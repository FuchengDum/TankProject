"""
游戏配置模块

该模块集中管理所有游戏常量和配置参数
遵循 KISS 原则，消除魔法数字，提高可维护性
"""


class GameConfig:
    """游戏核心配置常量"""

    # 窗口配置
    WINDOW_WIDTH = 750
    WINDOW_HEIGHT = 630
    WINDOW_SIZE = (WINDOW_WIDTH, WINDOW_HEIGHT)
    WINDOW_TITLE = "Tank War"

    # 游戏配置
    TOTAL_LEVELS = 35
    FPS = 60  # 帧率

    # 敌军配置
    DEFAULT_ENEMY_COUNT = 20  # 默认敌军剩余数量
    MAX_ENEMY_ON_SCREEN = 3   # 屏幕上最多同时存在的敌人数量

    # 无敌时间配置（帧数）
    INVINCIBLE_DURATION_T1 = 200  # 玩家1无敌时长
    INVINCIBLE_DURATION_T2 = 200  # 玩家2无敌时长

    # 基地铁墙时间
    IRON_BASE_DURATION = 0  # 基地铁墙持续时间


class TimerConfig:
    """定时器事件配置"""

    # 延迟时间（毫秒）
    ENEMY_SPAWN_DELAY = 200      # 敌方坦克生成延迟
    ENEMY_BULLET_COOLDOWN = 1000 # 敌方子弹冷却时间
    PLAYER_BULLET_COOLDOWN = 200 # 玩家子弹冷却时间
    ENEMY_STOP_INTERVAL = 8000   # 敌方坦克静止间隔

    # 其他延迟
    DEFAULT_DELAY = 100


class PathConfig:
    """资源路径配置"""

    # 图片路径
    IMAGE_DIR = "image"
    MUSIC_DIR = "music"

    # 背景图片
    BACKGROUND_IMAGE = f"{IMAGE_DIR}/background.png"
    BACKGROUND_LEVEL_MODE = f"{IMAGE_DIR}/background_level_model_tishi.png"
    BACKGROUND_ENDLESS_MODE = f"{IMAGE_DIR}/background_endless_mode_tishi.png"
    BACKGROUND_HEADS_UP_MODE = f"{IMAGE_DIR}/background_heads_up_mode_tishi.png"

    # UI 图标
    ENEMY_ICON = f"{IMAGE_DIR}/enemy.png"
    HEART_ICON = f"{IMAGE_DIR}/heart.png"

    # 游戏结束图片
    GAME_OVER_PLAYER1_WIN = f"{IMAGE_DIR}/game_over_player1_win.png"
    GAME_OVER_PLAYER2_WIN = f"{IMAGE_DIR}/game_over_player2_win.png"
    GAME_OVER_WIN = f"{IMAGE_DIR}/game_over_win.png"
    GAME_OVER_FAIL = f"{IMAGE_DIR}/game_over_fail.png"

    # 暂停图片
    GAME_PAUSE_IMAGE = f"{IMAGE_DIR}/game_pause.png"

    # 音效路径
    SOUND_BANG = f"{MUSIC_DIR}/bang.wav"
    SOUND_START = f"{MUSIC_DIR}/start.wav"
    SOUND_ADD = f"{MUSIC_DIR}/add.wav"
    SOUND_ATTACK = f"{MUSIC_DIR}/attack.mp3"
    SOUND_GET_PROPS = f"{MUSIC_DIR}/get_props.mp3"
    SOUND_PROP = f"{MUSIC_DIR}/prop.mp3"
    SOUND_PROP_BOOM = f"{MUSIC_DIR}/prop_boom.wav"
    SOUND_WALL = f"{MUSIC_DIR}/wall.mp3"


class SoundConfig:
    """音效配置"""

    # 音量设置（0.0 - 1.0）
    BANG_VOLUME = 1.0
    DEFAULT_VOLUME = 1.0

    # 是否启用音效
    SOUND_ENABLED = True


class UIConfig:
    """UI 显示配置"""

    # 暂停界面位置
    PAUSE_IMAGE_X = 250
    PAUSE_IMAGE_Y = 250

    # 其他 UI 配置可以在这里添加
