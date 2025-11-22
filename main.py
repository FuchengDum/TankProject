"""
坦克大战游戏主入口模块

修改时间：2021.12.15
修改人：2019051604048 詹孝东
模块描述：
此模块是游戏的入口文件也是游戏的菜单实现模块
包括主菜单、关卡模式选择菜单、无尽模式选择菜单、单挑模式选择菜单、建造模式选择菜单
"""

import sys
import pygame
import pygame_menu
import game_loader
from path_utils import resource_path


# ============================================================================
# 配置常量 - 集中管理所有魔法数字（KISS 原则）
# ============================================================================
class GameConfig:
    """游戏配置常量类 - 单一职责：管理配置"""

    # 窗口配置
    WINDOW_WIDTH = 750
    WINDOW_HEIGHT = 630
    WINDOW_SIZE = (WINDOW_WIDTH, WINDOW_HEIGHT)

    # 游戏配置
    TOTAL_LEVELS = 35

    # 加载动画配置
    INIT_IMAGES_COUNT = 105  # 实际图片数量（1-105）
    INIT_IMAGE_STEP = 3      # 加载动画步进
    INIT_IMAGE_X = 10        # 加载图片 X 坐标
    INIT_IMAGE_Y_FIRST = 20  # 第一帧 Y 坐标
    INIT_IMAGE_Y = 25        # 后续帧 Y 坐标

    # 菜单配置
    MAIN_MENU_FONT_SIZE = 30
    SUBMENU_FONT_SIZE = 25
    LOGO_SCALE = (1.2, 1.2)

    # 按钮配置
    BUTTON_PADDING = (80, 80, 80, 80)
    BUTTON_MARGIN = (10, 10)
    BUTTON_BORDER_INFLATE = (10, 10)
    BUTTON_BORDER_WIDTH = 10
    LABEL_VERTICAL_MARGIN = 20

    # 建造模式关卡编号
    CUSTOM_LEVEL_MODE = 99
    CUSTOM_HEADS_UP_MODE = 88

# ============================================================================
# 游戏模式函数 - 单一职责：启动不同游戏模式
# ============================================================================
def level_mode(level: int) -> None:
    """
    关卡模式：从指定关卡开始，顺序通关 1-35 关

    Args:
        level: 起始关卡编号 (1-35)
    """
    current = level
    while 1 <= current <= GameConfig.TOTAL_LEVELS:
        game = game_loader.Game()
        result = game.game_running(current, False)
        if result == "NEXT":
            current += 1
        else:
            break


def endless_mode(level: int) -> None:
    """
    无尽模式：在指定关卡无限挑战

    Args:
        level: 关卡编号
    """
    game = game_loader.Game()
    game.game_running(level, True)


def heads_up_mode(level: int) -> None:
    """
    单挑模式：单独挑战指定关卡

    Args:
        level: 关卡编号
    """
    game = game_loader.Game()
    game.game_running_singled_out(level)


# ============================================================================
# 资源加载器 - 单一职责：管理资源加载（DRY 原则）
# ============================================================================
class ResourceLoader:
    """资源加载管理器 - 负责所有资源的加载和错误处理"""

    @staticmethod
    def load_init_images() -> list:
        """
        加载初始化动画图片（1-105）

        Returns:
            图片列表，索引 0 为 None，1-105 为实际图片

        Note:
            如果某张图片加载失败，会创建占位图片避免崩溃
        """
        images = [None] * (GameConfig.INIT_IMAGES_COUNT + 1)

        for i in range(1, GameConfig.INIT_IMAGES_COUNT + 1):
            try:
                path = resource_path(f"image/init/init{i}.png")
                images[i] = pygame.image.load(path)
            except (pygame.error, FileNotFoundError) as e:
                print(f"警告：无法加载初始化图片 {i}: {e}")
                # 创建 1x1 透明占位图片，避免程序崩溃
                images[i] = pygame.Surface((1, 1))
                images[i].set_alpha(0)

        return images

    @staticmethod
    def load_logo() -> str:
        """
        获取 Logo 图片路径

        Returns:
            Logo 图片的完整路径
        """
        return resource_path("image/logo.png")

    @staticmethod
    def get_level_map_image(level: int) -> str:
        """
        获取关卡地图预览图路径

        Args:
            level: 关卡编号

        Returns:
            地图预览图的完整路径
        """
        return resource_path(f"image/maps/Battle-City-{level}.png")


# ============================================================================
# 加载动画管理器 - 单一职责：显示加载进度
# ============================================================================
class LoadingScreen:
    """加载屏幕管理器 - 负责显示加载动画"""

    def __init__(self, surface: pygame.Surface, images: list):
        """
        初始化加载屏幕

        Args:
            surface: 游戏窗口表面
            images: 加载动画图片列表
        """
        self.surface = surface
        self.images = images
        self.current_index = 1

    def show_initial_frame(self) -> None:
        """显示第一帧加载画面"""
        if self.images[1]:
            self.surface.blit(
                self.images[1],
                (GameConfig.INIT_IMAGE_X, GameConfig.INIT_IMAGE_Y_FIRST)
            )
            pygame.display.flip()

    def animate_loading(self, steps: int = GameConfig.TOTAL_LEVELS) -> None:
        """
        播放加载动画

        Args:
            steps: 动画步数（默认为关卡总数）
        """
        for _ in range(steps):
            # 处理退出事件，避免加载时无法关闭窗口
            for event in pygame.event.get():
                if event.type == pygame.QUIT:
                    pygame.quit()
                    sys.exit()

            # 显示当前帧
            if (self.current_index < len(self.images) and
                self.images[self.current_index]):
                self.surface.blit(
                    self.images[self.current_index],
                    (GameConfig.INIT_IMAGE_X, GameConfig.INIT_IMAGE_Y)
                )
                pygame.display.flip()

            # 更新索引，确保不越界
            self.current_index = min(
                self.current_index + GameConfig.INIT_IMAGE_STEP,
                len(self.images) - 1
            )


# ============================================================================
# 菜单构建器 - 单一职责：创建和配置菜单（DRY 原则）
# ============================================================================
class MenuBuilder:
    """菜单构建器 - 统一管理所有菜单的创建"""

    @staticmethod
    def create_theme(font_size: int = GameConfig.MAIN_MENU_FONT_SIZE) -> pygame_menu.Theme:
        """
        创建统一的菜单主题（消除重复代码）

        Args:
            font_size: 字体大小

        Returns:
            配置好的主题对象
        """
        theme = pygame_menu.themes.THEME_DARK.copy()
        theme.background_color = (0, 0, 0)
        theme.widget_font = pygame_menu.font.FONT_8BIT
        theme.title_background_color = (0, 0, 0)
        theme.widget_font_size = font_size
        return theme

    @staticmethod
    def add_level_buttons(menu: pygame_menu.Menu, mode_func: callable) -> None:
        """
        为菜单添加关卡选择按钮（1-35 关）

        Args:
            menu: 目标菜单对象
            mode_func: 点击按钮时调用的游戏模式函数
        """
        for level in range(1, GameConfig.TOTAL_LEVELS + 1):
            try:
                # 加载关卡预览图作为按钮背景
                background_image = pygame_menu.BaseImage(
                    image_path=ResourceLoader.get_level_map_image(level)
                )

                # 添加关卡标签
                menu.add.label(f"{level}  Checkpoint")
                menu.add.vertical_margin(GameConfig.LABEL_VERTICAL_MARGIN)

                # 添加关卡按钮
                menu.add.button(
                    '',  # 空标题，使用背景图
                    mode_func,
                    level,
                    background_color=background_image,
                    padding=GameConfig.BUTTON_PADDING,
                    margin=GameConfig.BUTTON_MARGIN,
                    border_inflate=GameConfig.BUTTON_BORDER_INFLATE,
                    border_width=GameConfig.BUTTON_BORDER_WIDTH
                )
            except Exception as e:
                print(f"警告：无法添加关卡 {level} 按钮: {e}")

    @staticmethod
    def create_level_mode_menu() -> pygame_menu.Menu:
        """
        创建关卡模式菜单

        Returns:
            配置好的关卡模式菜单
        """
        theme = MenuBuilder.create_theme(GameConfig.SUBMENU_FONT_SIZE)
        menu = pygame_menu.Menu(
            'Level Mode Menu:Choose a level',
            GameConfig.WINDOW_WIDTH,
            GameConfig.WINDOW_HEIGHT,
            theme=theme
        )
        MenuBuilder.add_level_buttons(menu, level_mode)
        return menu

    @staticmethod
    def create_endless_mode_menu() -> pygame_menu.Menu:
        """
        创建无尽模式菜单

        Returns:
            配置好的无尽模式菜单
        """
        theme = MenuBuilder.create_theme(GameConfig.SUBMENU_FONT_SIZE)
        menu = pygame_menu.Menu(
            'Endless Mode Menu:Choose a level',
            GameConfig.WINDOW_WIDTH,
            GameConfig.WINDOW_HEIGHT,
            theme=theme
        )
        MenuBuilder.add_level_buttons(menu, endless_mode)
        return menu

    @staticmethod
    def create_heads_up_menu() -> pygame_menu.Menu:
        """
        创建单挑模式菜单

        Returns:
            配置好的单挑模式菜单
        """
        theme = MenuBuilder.create_theme(GameConfig.SUBMENU_FONT_SIZE)
        menu = pygame_menu.Menu(
            'Heads up Mode Menu:Choose a level',
            GameConfig.WINDOW_WIDTH,
            GameConfig.WINDOW_HEIGHT,
            center_content=True,
            theme=theme
        )
        MenuBuilder.add_level_buttons(menu, heads_up_mode)
        return menu

    @staticmethod
    def create_custom_mode_menu() -> pygame_menu.Menu:
        """
        创建建造模式菜单

        Returns:
            配置好的建造模式菜单
        """
        theme = MenuBuilder.create_theme(GameConfig.SUBMENU_FONT_SIZE)
        menu = pygame_menu.Menu(
            'Custom mode Menu:Choose a mode',
            GameConfig.WINDOW_WIDTH,
            GameConfig.WINDOW_HEIGHT,
            theme=theme
        )
        menu.add.button('Level mode', level_mode, GameConfig.CUSTOM_LEVEL_MODE)
        menu.add.button('Endless mode', endless_mode, GameConfig.CUSTOM_LEVEL_MODE)
        menu.add.button('Heads up mode', heads_up_mode, GameConfig.CUSTOM_HEADS_UP_MODE)
        return menu

    @staticmethod
    def create_main_menu(
        level_menu: pygame_menu.Menu,
        endless_menu: pygame_menu.Menu,
        heads_up_menu: pygame_menu.Menu,
        custom_menu: pygame_menu.Menu
    ) -> pygame_menu.Menu:
        """
        创建主菜单

        Args:
            level_menu: 关卡模式菜单
            endless_menu: 无尽模式菜单
            heads_up_menu: 单挑模式菜单
            custom_menu: 建造模式菜单

        Returns:
            配置好的主菜单
        """
        theme = MenuBuilder.create_theme()
        menu = pygame_menu.Menu(
            '',  # 空标题
            GameConfig.WINDOW_WIDTH,
            GameConfig.WINDOW_HEIGHT,
            theme=theme
        )

        # 添加 Logo
        try:
            menu.add.image(
                ResourceLoader.load_logo(),
                scale=GameConfig.LOGO_SCALE
            )
        except Exception as e:
            print(f"警告：无法加载 Logo: {e}")

        # 添加菜单按钮
        menu.add.button('Level mode', level_menu)
        menu.add.button('Endless mode', endless_menu)
        menu.add.button('Heads up mode', heads_up_menu)
        menu.add.button('Custom mode', custom_menu)
        menu.add.button('Quit', pygame_menu.events.EXIT)

        return menu


# ============================================================================
# 主函数 - 单一职责：协调各组件启动游戏
# ============================================================================
def main() -> None:
    """
    主函数 - 程序入口点

    职责：
    1. 初始化 pygame
    2. 创建游戏窗口
    3. 显示加载动画
    4. 创建菜单系统
    5. 启动主循环
    """
    try:
        # 1. 初始化 pygame
        pygame.init()
        surface = pygame.display.set_mode(GameConfig.WINDOW_SIZE)
        pygame.display.set_caption("Tank War")

        # 2. 加载资源
        init_images = ResourceLoader.load_init_images()

        # 3. 显示加载动画
        loading_screen = LoadingScreen(surface, init_images)
        loading_screen.show_initial_frame()
        loading_screen.animate_loading()

        # 4. 创建菜单系统
        level_menu = MenuBuilder.create_level_mode_menu()
        endless_menu = MenuBuilder.create_endless_mode_menu()
        heads_up_menu = MenuBuilder.create_heads_up_menu()
        custom_menu = MenuBuilder.create_custom_mode_menu()
        main_menu = MenuBuilder.create_main_menu(
            level_menu, endless_menu, heads_up_menu, custom_menu
        )

        # 5. 清屏并启动主菜单循环
        surface.fill((0, 0, 0))
        main_menu.mainloop(surface)

    except Exception as e:
        print(f"游戏启动失败: {e}")
        import traceback
        traceback.print_exc()
        sys.exit(1)


# ============================================================================
# 程序入口
# ============================================================================
if __name__ == "__main__":
    main()