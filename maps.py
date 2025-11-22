"""
地图模块（重构版）

修改时间：2021.12.15
修改人：2019051604048 詹孝东

重构时间：2025.11.22
重构说明：将地图数据提取到 JSON 文件，大幅简化代码

模块描述：
该模块是地图类，聚合了墙体类
主要功能：
1. 从 JSON 文件加载地图数据
2. 将地图数据翻译成墙体对象
3. 支持 35 个关卡 + 自定义地图

重构改进：
- 将 1200+ 行地图数据提取到 JSON 文件
- 代码从 1217 行减少到约 150 行
- 易于添加新关卡（只需编辑 JSON）
- 符合数据与代码分离原则
"""

import pygame
import wall
import json
import os
from path_utils import resource_path


class MapLoader:
    """
    地图加载器 - 单一职责：加载地图数据

    职责：
    1. 从 JSON 文件加载地图数据
    2. 缓存已加载的地图
    3. 提供地图数据访问接口
    """

    def __init__(self):
        """初始化地图加载器"""
        self._maps_cache = {}
        self._load_maps()

    def _load_maps(self) -> None:
        """从 JSON 文件加载所有地图数据"""
        try:
            maps_file = resource_path('maps/levels.json')
            with open(maps_file, 'r', encoding='utf-8') as f:
                self._maps_cache = json.load(f)
            print(f"成功加载 {len(self._maps_cache)} 个地图")
        except FileNotFoundError:
            print("警告：未找到地图文件 maps/levels.json，使用默认空地图")
            self._maps_cache = {'template': self._create_empty_map()}
        except json.JSONDecodeError as e:
            print(f"警告：地图文件格式错误: {e}，使用默认空地图")
            self._maps_cache = {'template': self._create_empty_map()}

    def _create_empty_map(self) -> list:
        """创建空地图模板"""
        return [[0 for _ in range(26)] for _ in range(26)]

    def get_map(self, level: int) -> list:
        """
        获取指定关卡的地图数据

        Args:
            level: 关卡编号 (1-35, 51, 88, 99)

        Returns:
            地图数据（二维数组）
        """
        # 特殊关卡映射
        if level == 99 or level == 88:
            map_key = 'template'
        elif level == 51:
            map_key = 'level_51'
        else:
            map_key = f'level_{level}'

        # 返回地图数据，如果不存在则返回模板
        return self._maps_cache.get(map_key, self._maps_cache.get('template', self._create_empty_map()))


class Map:
    """
    地图类（重构版）- 单一职责：管理地图墙体

    职责：
    1. 管理各种墙体精灵组
    2. 将地图数据翻译成墙体对象
    3. 提供地图绘制接口
    """

    # 地图元素类型映射
    TILE_TYPES = {
        0: None,        # 空地
        1: 'brick',     # 砖块
        2: 'iron',      # 铁块
        3: 'tree',      # 树
        4: 'river',     # 河流
        5: 'ice',       # 冰面
    }

    def __init__(self):
        """初始化地图"""
        # 墙体精灵组
        self.brickGroup = pygame.sprite.Group()
        self.ironGroup = pygame.sprite.Group()
        self.riverGroup = pygame.sprite.Group()
        self.treeGroup = pygame.sprite.Group()
        self.iceGroup = pygame.sprite.Group()
        self.homeGroup = pygame.sprite.Group()

        # 地图加载器
        self._loader = MapLoader()

    def draw(self, map_data: list, is_normal_mode: bool = True) -> None:
        """
        将地图数据翻译成墙体对象

        Args:
            map_data: 地图数据（二维数组）
            is_normal_mode: 是否为普通模式（True=普通模式，False=单挑模式）
        """
        # 清空现有墙体
        self.brickGroup.empty()
        self.ironGroup.empty()
        self.riverGroup.empty()
        self.treeGroup.empty()
        self.iceGroup.empty()
        self.homeGroup.empty()

        # 绘制基地（只在普通模式下）
        if is_normal_mode:
            home = wall.Home()
            home.rect.left, home.rect.top = 3 + 12 * 24, 3 + 24 * 24
            self.homeGroup.add(home)

        # 遍历地图数据，创建墙体对象
        for x in range(len(map_data)):
            for y in range(len(map_data[0])):
                tile_type = map_data[x][y]

                # 根据类型创建对应的墙体
                if tile_type == 1:  # 砖块
                    brick = wall.Brick()
                    brick.rect.left, brick.rect.top = 3 + y * 24, 3 + x * 24
                    self.brickGroup.add(brick)

                elif tile_type == 2:  # 铁块
                    iron = wall.Iron()
                    iron.rect.left, iron.rect.top = 3 + y * 24, 3 + x * 24
                    self.ironGroup.add(iron)

                elif tile_type == 3:  # 树
                    tree = wall.Tree()
                    tree.rect.left, tree.rect.top = 3 + y * 24, 3 + x * 24
                    self.treeGroup.add(tree)

                elif tile_type == 4:  # 河流
                    river = wall.River()
                    river.rect.left, river.rect.top = 3 + y * 24, 3 + x * 24
                    self.riverGroup.add(river)

                elif tile_type == 5:  # 冰面
                    ice = wall.Ice()
                    ice.rect.left, ice.rect.top = 3 + y * 24, 3 + x * 24
                    self.iceGroup.add(ice)

    def checkpoint(self, checkpoint_n: int, own_map: list = None) -> None:
        """
        加载指定关卡的地图

        Args:
            checkpoint_n: 关卡编号
                - 1-35: 正常关卡
                - 51: 单挑模式关卡
                - 88: 单挑模式自定义关卡
                - 99: 普通模式自定义关卡
            own_map: 自定义地图数据（可选）
        """
        # 地图元素说明：
        # 0 - 普通地面
        # 1 - 砖块
        # 2 - 铁块
        # 3 - 树
        # 4 - 河流
        # 5 - 冰面

        # 判断是否为普通模式
        is_normal_mode = checkpoint_n != 88

        # 使用自定义地图或加载预设地图
        if own_map is not None:
            map_data = own_map
        else:
            map_data = self._loader.get_map(checkpoint_n)

        # 绘制地图
        self.draw(map_data, is_normal_mode)
