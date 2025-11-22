"""
资源管理模块

该模块负责统一管理游戏资源的加载
包括图片、音效等资源，提供错误处理和容错机制
遵循单一职责原则（SRP）
"""

import pygame
from typing import Dict, Optional
from path_utils import resource_path
from config import PathConfig, SoundConfig


class ResourceManager:
    """
    资源管理器 - 单一职责：管理所有游戏资源的加载

    职责：
    1. 加载图片资源
    2. 加载音效资源
    3. 提供错误处理和容错机制
    4. 缓存已加载的资源
    """

    def __init__(self):
        """初始化资源管理器"""
        self.images: Dict[str, pygame.Surface] = {}
        self.sounds: Dict[str, pygame.mixer.Sound] = {}
        self._load_all_resources()

    def _load_all_resources(self) -> None:
        """加载所有游戏资源"""
        self._load_images()
        self._load_sounds()

    def _load_images(self) -> None:
        """加载所有图片资源"""
        image_paths = {
            'background': PathConfig.BACKGROUND_IMAGE,
            'background_level_mode': PathConfig.BACKGROUND_LEVEL_MODE,
            'background_endless_mode': PathConfig.BACKGROUND_ENDLESS_MODE,
            'background_heads_up_mode': PathConfig.BACKGROUND_HEADS_UP_MODE,
            'enemy_icon': PathConfig.ENEMY_ICON,
            'heart_icon': PathConfig.HEART_ICON,
            'game_over_player1_win': PathConfig.GAME_OVER_PLAYER1_WIN,
            'game_over_player2_win': PathConfig.GAME_OVER_PLAYER2_WIN,
            'game_over_win': PathConfig.GAME_OVER_WIN,
            'game_over_fail': PathConfig.GAME_OVER_FAIL,
            'game_pause': PathConfig.GAME_PAUSE_IMAGE,
        }

        for name, path in image_paths.items():
            self.images[name] = self._load_image(path, name)

    def _load_sounds(self) -> None:
        """加载所有音效资源"""
        sound_paths = {
            'bang': PathConfig.SOUND_BANG,
            'start': PathConfig.SOUND_START,
            'add': PathConfig.SOUND_ADD,
            'attack': PathConfig.SOUND_ATTACK,
            'get_props': PathConfig.SOUND_GET_PROPS,
            'prop': PathConfig.SOUND_PROP,
            'prop_boom': PathConfig.SOUND_PROP_BOOM,
            'wall': PathConfig.SOUND_WALL,
        }

        for name, path in sound_paths.items():
            self.sounds[name] = self._load_sound(path, name)

        # 设置音量
        if 'bang' in self.sounds:
            self.sounds['bang'].set_volume(SoundConfig.BANG_VOLUME)

    def _load_image(self, path: str, name: str) -> Optional[pygame.Surface]:
        """
        加载单个图片资源

        Args:
            path: 图片相对路径
            name: 资源名称（用于日志）

        Returns:
            加载的图片 Surface，失败则返回占位图片
        """
        try:
            full_path = resource_path(path)
            image = pygame.image.load(full_path)
            return image
        except (pygame.error, FileNotFoundError) as e:
            print(f"警告：无法加载图片 '{name}' ({path}): {e}")
            # 创建占位图片（100x100 红色方块）
            placeholder = pygame.Surface((100, 100))
            placeholder.fill((255, 0, 0))
            return placeholder

    def _load_sound(self, path: str, name: str) -> Optional[pygame.mixer.Sound]:
        """
        加载单个音效资源

        Args:
            path: 音效相对路径
            name: 资源名称（用于日志）

        Returns:
            加载的音效 Sound，失败则返回静音占位
        """
        try:
            full_path = resource_path(path)
            sound = pygame.mixer.Sound(full_path)
            return sound
        except (pygame.error, FileNotFoundError) as e:
            print(f"警告：无法加载音效 '{name}' ({path}): {e}")
            # 创建静音占位（1秒静音）
            try:
                # 创建一个空的音频缓冲区
                import numpy as np
                sample_rate = 22050
                duration = 0.1  # 0.1秒
                samples = np.zeros(int(sample_rate * duration), dtype=np.int16)
                sound = pygame.sndarray.make_sound(samples)
                return sound
            except:
                # 如果 numpy 不可用，返回 None
                return None

    # ========================================================================
    # 公共接口 - 获取资源
    # ========================================================================

    def get_image(self, name: str) -> Optional[pygame.Surface]:
        """
        获取图片资源

        Args:
            name: 图片名称

        Returns:
            图片 Surface，不存在则返回 None
        """
        return self.images.get(name)

    def get_sound(self, name: str) -> Optional[pygame.mixer.Sound]:
        """
        获取音效资源

        Args:
            name: 音效名称

        Returns:
            音效 Sound，不存在则返回 None
        """
        return self.sounds.get(name)

    def play_sound(self, name: str) -> None:
        """
        播放音效

        Args:
            name: 音效名称
        """
        if not SoundConfig.SOUND_ENABLED:
            return

        sound = self.get_sound(name)
        if sound:
            try:
                sound.play()
            except pygame.error as e:
                print(f"警告：无法播放音效 '{name}': {e}")

    def stop_sound(self, name: str) -> None:
        """
        停止音效

        Args:
            name: 音效名称
        """
        sound = self.get_sound(name)
        if sound:
            try:
                sound.stop()
            except pygame.error as e:
                print(f"警告：无法停止音效 '{name}': {e}")

    # ========================================================================
    # 便捷方法 - 直接访问常用资源
    # ========================================================================

    @property
    def background_image(self) -> pygame.Surface:
        """获取背景图片"""
        return self.get_image('background')

    @property
    def enemy_icon(self) -> pygame.Surface:
        """获取敌人图标"""
        return self.get_image('enemy_icon')

    @property
    def heart_icon(self) -> pygame.Surface:
        """获取生命图标"""
        return self.get_image('heart_icon')

    @property
    def game_pause_image(self) -> pygame.Surface:
        """获取暂停图片"""
        return self.get_image('game_pause')

    def get_background_for_mode(self, mode: str) -> pygame.Surface:
        """
        根据游戏模式获取对应的背景图片

        Args:
            mode: 游戏模式 ('level', 'endless', 'heads_up')

        Returns:
            对应模式的背景图片
        """
        mode_map = {
            'level': 'background_level_mode',
            'endless': 'background_endless_mode',
            'heads_up': 'background_heads_up_mode',
        }
        bg_name = mode_map.get(mode, 'background')
        return self.get_image(bg_name)

    def get_game_over_image(self, result: str) -> pygame.Surface:
        """
        根据游戏结果获取对应的结束图片

        Args:
            result: 游戏结果 ('player1_win', 'player2_win', 'win', 'fail')

        Returns:
            对应结果的游戏结束图片
        """
        image_name = f'game_over_{result}'
        return self.get_image(image_name)
