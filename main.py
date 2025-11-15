import pygame
import pygame_menu
import game_loader
import sys
from path_utils import resource_path
"""
修改时间：2021.12.15
修改人：2019051604048 詹孝东
模块描述：
此模块是游戏的入口文件也是游戏的菜单实现模块
模块包括主菜单、关卡模式选择菜单、无尽模式选择菜单、单挑模式选择菜单、建造模式选择菜单
点击相应按钮调用相应下面相应函数进行实现，详情请看下面
"""
# 关卡模式
def Level_mode(i):
    # Loop levels 1-35; if entered at mid-level, continue from there
    current = i
    while 1 <= current <= 35:
        game = game_loader.Game()
        result = game.game_running(current, False)
        if result == "NEXT":
            current += 1
            continue
        else:
            break
# 无尽模式
def endless_mode(i):
    game = game_loader.Game()
    game.game_running(i,True)
# 单挑模式
def heads_up_mode(i):
    game = game_loader.Game()
    game.game_running_singled_out(i)
# 设置 未实现
def set_up():
    pass

# 优化：抽取菜单按钮创建为函数，减少冗余
def add_level_buttons(menu, mode_func):
    for i in range(1, 36):
        background_image = pygame_menu.BaseImage(
            image_path=resource_path(f"image/maps/Battle-City-{i}.png")
        )
        menu.add.label(f"{i}  Checkpoint")
        menu.add.vertical_margin(20)
        menu.add.button('', mode_func, i, background_color=background_image,
                        padding=(80, 80, 80, 80), margin=(10, 10), border_inflate=(10, 10), border_width=(10))
        
def main():
    # ------------------------------------
    # 创建window
    # -----------------------------------

    pygame.init()
    surface = pygame.display.set_mode((750, 630))

    # 创建加载进度条的图片
    init_image_all = [None]*106
    for i in range(1,106):
        init_image_all[i] = pygame.image.load(resource_path("image/init/init"+str(i)+".png"))

    # 显示加载进度条（第一张图片）
    surface.blit(init_image_all[1], (10, 20))
    pygame.display.flip()
    now_i = 1

    # -------------------------------------------------------------
    # 创建关卡模式菜单和无尽模式菜单
    # -------------------------------------------------------------
    # 创建选择关卡菜单的“主题”
    checkpoint_theme = pygame_menu.themes.THEME_DARK.copy()
    # 背景颜色
    checkpoint_theme.background_color = (0, 0, 0)
    # 字体样式
    checkpoint_theme.widget_font = pygame_menu.font.FONT_8BIT
    # 标题栏背景颜色
    checkpoint_theme.title_background_color = (0, 0, 0)
    checkpoint_theme.widget_font_size = 25

    # 加载进度条图片显示逻辑可单独处理，避免与菜单按钮创建混合
    for i in range(1, 36):
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                pygame.quit()
                sys.exit()
        surface.blit(init_image_all[now_i], (10, 25))
        now_i += 3
        pygame.display.flip()

    # 关卡模式“菜单”的创建
    level_mode_menu = pygame_menu.Menu('Level Mode Menu:Choose a level', 750, 630, theme=checkpoint_theme)
    add_level_buttons(level_mode_menu, Level_mode)
    # 无尽模式“菜单”的创建
    endless_mode_menu = pygame_menu.Menu('Endless Mode Menu:Choose a level', 750, 630, theme=checkpoint_theme)
    add_level_buttons(endless_mode_menu, endless_mode)


    # 单挑模式菜单的创建 （因为目前只有一关 所以只加一个按钮）
    heads_up_menu = pygame_menu.Menu('Heads up Mode Menu:Choose a level', 750, 630,center_content = True, theme=checkpoint_theme)
    add_level_buttons(heads_up_menu, heads_up_mode)
    # background_image = pygame_menu.BaseImage(
    #     image_path=resource_path("image/maps/Battle-City-51.png")
    # )
    # heads_up_menu.add.label("heads up Checkpoint")
    # heads_up_menu.add.vertical_margin(20)
    # heads_up_menu.add.button('', heads_up_mode, 51, background_color=background_image,
    #                            padding=(80, 80, 80, 80), margin=(10, 10), border_inflate=(10, 10), border_width=(10))

    # 建造模式菜单创建
    custom_mode_menu = pygame_menu.Menu('Custom mode Menu:Choose a mode', 750, 630, theme=checkpoint_theme)
    custom_mode_menu.add.button('Level mode',Level_mode,99)
    custom_mode_menu.add.button('endless mode', endless_mode,99)
    custom_mode_menu.add.button('heads up mode', heads_up_mode,88)

    # -----------------------------------------------------------------
    # 创建主菜单
    # -----------------------------------------------------------------
    # 创建主菜单“主题”
    main_theme = pygame_menu.themes.THEME_DARK.copy()
    # 背景颜色
    main_theme.background_color = (0, 0, 0)
    # 字体样式
    main_theme.widget_font = pygame_menu.font.FONT_8BIT
    # 标题栏背景颜色
    main_theme.title_background_color = (0, 0, 0)
    main_theme.widget_font_size = 30
    # 创建主“菜单”
    main_menu = pygame_menu.Menu('', 750, 630, theme=main_theme)

    image_path = resource_path("image/logo.png")
    main_menu.add.image(image_path,scale = (1.2,1.2))
    main_menu.add.button('Level mode', level_mode_menu)
    main_menu.add.button('Endless mode', endless_mode_menu)
    main_menu.add.button('Heads up mode',heads_up_menu)
    main_menu.add.button('Custom mode', custom_mode_menu)
    main_menu.add.button('Set up', set_up)
    main_menu.add.button('Quit', pygame_menu.events.EXIT)
    # ------------------------------------------------------------------
    # 主循环 此方法相当于循环函数一直循环此主菜单
    # ---------------------------------------------------------------
    # 清掉任何加载阶段遗留的像素，避免顶部灰条
    surface.fill((0, 0, 0))
    main_menu.mainloop(surface)
# 调用main函数
if __name__ == "__main__":
    main()