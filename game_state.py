"""
游戏状态管理模块

该模块负责管理游戏运行时的所有状态变量
遵循单一职责原则（SRP）
"""

import pygame
from config import GameConfig, TimerConfig


class GameState:
    """
    游戏状态管理器 - 单一职责：管理游戏状态

    职责：
    1. 管理游戏运行状态（暂停、结束等）
    2. 管理玩家状态（生命、无敌等）
    3. 管理敌人状态（剩余数量、移动等）
    4. 管理道具和特效状态
    """

    def __init__(self):
        """初始化游戏状态"""
        # 游戏模式
        self.is_endless = False  # 是否为无尽模式

        # 游戏运行状态
        self.is_paused = False
        self.is_game_over = False
        self.game_result = None  # 'win', 'fail', 'player1_win', 'player2_win'

        # 玩家状态
        self.player1_running = True  # 玩家1是否存活
        self.player2_running = True  # 玩家2是否存活
        self.invincible_t1 = GameConfig.INVINCIBLE_DURATION_T1  # 玩家1无敌时间
        self.invincible_t2 = GameConfig.INVINCIBLE_DURATION_T2  # 玩家2无敌时间

        # 玩家移动状态
        self.player1_moving = 0  # 玩家1移动状态
        self.player1_direction = 0  # 玩家1移动方向
        self.player2_moving = 0  # 玩家2移动状态
        self.player2_direction = 0  # 玩家2移动方向

        # 敌人状态
        self.remaining_enemy = GameConfig.DEFAULT_ENEMY_COUNT  # 剩余敌人数量
        self.enemy_on_screen = GameConfig.MAX_ENEMY_ON_SCREEN  # 屏幕上的敌人数量
        self.enemy_can_move = True  # 敌人是否可以移动

        # 子弹冷却状态
        self.enemy_bullet_cooling = True  # 敌方子弹是否冷却中
        self.player_bullet_cooling = True  # 玩家子弹是否冷却中

        # 特效和道具
        self.switch_r1_r2_image = True  # 用于切换图片动画
        self.iron_base_time = GameConfig.IRON_BASE_DURATION  # 基地铁墙剩余时间

        # 延迟和计时
        self.delay = TimerConfig.DEFAULT_DELAY

        # 音效开关
        self.sound_enabled = True

    # ========================================================================
    # 游戏状态查询
    # ========================================================================

    def is_player1_alive(self) -> bool:
        """玩家1是否存活"""
        return self.player1_running

    def is_player2_alive(self) -> bool:
        """玩家2是否存活"""
        return self.player2_running

    def is_any_player_alive(self) -> bool:
        """是否有玩家存活"""
        return self.player1_running or self.player2_running

    def is_player1_invincible(self) -> bool:
        """玩家1是否无敌"""
        return self.invincible_t1 > 0

    def is_player2_invincible(self) -> bool:
        """玩家2是否无敌"""
        return self.invincible_t2 > 0

    def has_enemies_remaining(self) -> bool:
        """是否还有剩余敌人"""
        return self.remaining_enemy > 0

    def can_spawn_enemy(self) -> bool:
        """是否可以生成新敌人"""
        return (self.has_enemies_remaining() and
                self.enemy_on_screen < GameConfig.MAX_ENEMY_ON_SCREEN)

    # ========================================================================
    # 游戏状态更新
    # ========================================================================

    def update_invincibility(self) -> None:
        """更新无敌状态"""
        if self.invincible_t1 > 0:
            self.invincible_t1 -= 1
        if self.invincible_t2 > 0:
            self.invincible_t2 -= 1

    def update_iron_base(self) -> None:
        """更新基地铁墙状态"""
        if self.iron_base_time > 0:
            self.iron_base_time -= 1

    def decrease_remaining_enemy(self) -> None:
        """减少剩余敌人数量"""
        if self.remaining_enemy > 0:
            self.remaining_enemy -= 1

    def increase_enemy_on_screen(self) -> None:
        """增加屏幕上的敌人数量"""
        self.enemy_on_screen += 1

    def decrease_enemy_on_screen(self) -> None:
        """减少屏幕上的敌人数量"""
        if self.enemy_on_screen > 0:
            self.enemy_on_screen -= 1

    # ========================================================================
    # 玩家状态管理
    # ========================================================================

    def kill_player1(self) -> None:
        """玩家1死亡"""
        self.player1_running = False

    def kill_player2(self) -> None:
        """玩家2死亡"""
        self.player2_running = False

    def revive_player1(self) -> None:
        """复活玩家1"""
        self.player1_running = True
        self.invincible_t1 = GameConfig.INVINCIBLE_DURATION_T1

    def revive_player2(self) -> None:
        """复活玩家2"""
        self.player2_running = True
        self.invincible_t2 = GameConfig.INVINCIBLE_DURATION_T2

    def set_player1_invincible(self, duration: int = None) -> None:
        """
        设置玩家1无敌

        Args:
            duration: 无敌持续时间（帧数），None 则使用默认值
        """
        if duration is None:
            duration = GameConfig.INVINCIBLE_DURATION_T1
        self.invincible_t1 = duration

    def set_player2_invincible(self, duration: int = None) -> None:
        """
        设置玩家2无敌

        Args:
            duration: 无敌持续时间（帧数），None 则使用默认值
        """
        if duration is None:
            duration = GameConfig.INVINCIBLE_DURATION_T2
        self.invincible_t2 = duration

    # ========================================================================
    # 游戏结束管理
    # ========================================================================

    def set_game_over_win(self) -> None:
        """设置游戏胜利"""
        self.is_game_over = True
        self.game_result = 'win'

    def set_game_over_fail(self) -> None:
        """设置游戏失败"""
        self.is_game_over = True
        self.game_result = 'fail'

    def set_game_over_player1_win(self) -> None:
        """设置玩家1胜利（单挑模式）"""
        self.is_game_over = True
        self.game_result = 'player1_win'

    def set_game_over_player2_win(self) -> None:
        """设置玩家2胜利（单挑模式）"""
        self.is_game_over = True
        self.game_result = 'player2_win'

    # ========================================================================
    # 道具效果管理
    # ========================================================================

    def activate_iron_base(self, duration: int = 500) -> None:
        """
        激活基地铁墙

        Args:
            duration: 持续时间（帧数）
        """
        self.iron_base_time = duration

    def is_base_iron(self) -> bool:
        """基地是否为铁墙"""
        return self.iron_base_time > 0

    # ========================================================================
    # 重置和初始化
    # ========================================================================

    def reset_for_new_level(self, is_endless: bool = False) -> None:
        """
        为新关卡重置状态

        Args:
            is_endless: 是否为无尽模式
        """
        self.is_endless = is_endless
        self.is_paused = False
        self.is_game_over = False
        self.game_result = None

        # 重置玩家状态
        self.player1_running = True
        self.player2_running = True
        self.invincible_t1 = GameConfig.INVINCIBLE_DURATION_T1
        self.invincible_t2 = GameConfig.INVINCIBLE_DURATION_T2

        # 重置敌人状态
        self.remaining_enemy = GameConfig.DEFAULT_ENEMY_COUNT
        self.enemy_on_screen = 0
        self.enemy_can_move = True

        # 重置其他状态
        self.iron_base_time = 0
        self.switch_r1_r2_image = True

    def update(self) -> None:
        """
        每帧更新游戏状态

        应在主游戏循环中调用
        """
        self.update_invincibility()
        self.update_iron_base()
