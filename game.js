// 游戏配置
const config = {
    canvasWidth: 800,
    canvasHeight: 600,
    tankSize: 40,
    bulletSize: 8,
    bulletSpeed: 7,
    tankSpeed: 3,
    enemySpeed: 2,
    enemySpawnInterval: 3000,
    maxEnemies: 4,
    wallTypes: {
        BRICK: { color: '#8B4513', health: 2 },
        STEEL: { color: '#696969', health: 4 },
        WATER: { color: '#1E90FF', health: Infinity }
    },
    difficultyIncrease: {
        speed: 0.2,
        interval: 200,
        maxSpeed: 5
    }
};

// 游戏状态
const gameState = {
    player: null,
    enemies: [],
    bullets: [],
    walls: [],
    base: null,
    score: 0,
    lives: 3,
    level: 1,
    isGameOver: false,
    isPaused: false,
    gameLoop: null,
    enemySpawner: null
};

// 获取Canvas上下文
const canvas = document.getElementById('gameCanvas');
const ctx = canvas.getContext('2d');

// 坦克类
class Tank {
    constructor(x, y, color, isPlayer = false) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.direction = 'UP';
        this.isPlayer = isPlayer;
        this.size = config.tankSize;
        this.speed = isPlayer ? config.tankSpeed : config.enemySpeed;
        this.lastShot = 0;
        this.shootDelay = isPlayer ? 500 : 2000;
        this.tracks = 0;
        this.health = isPlayer ? 3 : 1; // 玩家有3点生命值，敌人有1点
        this.isMoving = false;
        this.moveTimer = 0;
    }

    draw() {
        ctx.save();
        ctx.translate(this.x + this.size / 2, this.y + this.size / 2);
        
        // 根据方向旋转
        switch(this.direction) {
            case 'UP': ctx.rotate(0); break;
            case 'RIGHT': ctx.rotate(Math.PI / 2); break;
            case 'DOWN': ctx.rotate(Math.PI); break;
            case 'LEFT': ctx.rotate(-Math.PI / 2); break;
        }

        // 绘制坦克履带
        ctx.fillStyle = '#333';
        ctx.fillRect(-this.size / 2 - 5, -this.size / 2, this.size + 10, 5);
        ctx.fillRect(-this.size / 2 - 5, this.size / 2 - 5, this.size + 10, 5);
        
        // 履带纹理
        ctx.strokeStyle = '#666';
        ctx.lineWidth = 2;
        for (let i = 0; i < 5; i++) {
            ctx.beginPath();
            ctx.moveTo(-this.size / 2 + i * (this.size / 4), -this.size / 2);
            ctx.lineTo(-this.size / 2 + i * (this.size / 4), -this.size / 2 + 5);
            ctx.stroke();
            
            ctx.beginPath();
            ctx.moveTo(-this.size / 2 + i * (this.size / 4), this.size / 2 - 5);
            ctx.lineTo(-this.size / 2 + i * (this.size / 4), this.size / 2);
            ctx.stroke();
        }

        // 绘制坦克主体
        ctx.fillStyle = this.color;
        ctx.fillRect(-this.size / 2, -this.size / 2, this.size, this.size);
        
        // 坦克装甲细节
        ctx.strokeStyle = '#000';
        ctx.lineWidth = 2;
        ctx.strokeRect(-this.size / 2, -this.size / 2, this.size, this.size);
        
        // 坦克炮塔
        ctx.fillStyle = this.isPlayer ? '#0066cc' : '#cc0000';
        ctx.beginPath();
        ctx.arc(0, 0, this.size / 3, 0, Math.PI * 2);
        ctx.fill();
        ctx.stroke();
        
        // 炮管
        ctx.fillStyle = '#666';
        ctx.fillRect(-4, -this.size / 2 - 15, 8, 25);
        
        // 炮管阴影
        ctx.fillStyle = '#444';
        ctx.fillRect(-3, -this.size / 2 - 15, 6, 5);
        
        // 坦克标识（玩家坦克显示星星，敌方坦克显示骷髅）
        ctx.fillStyle = '#fff';
        if (this.isPlayer) {
            // 绘制星星
            ctx.beginPath();
            for (let i = 0; i < 5; i++) {
                const angle = (i * 2 * Math.PI / 5) - Math.PI / 2;
                const x = Math.cos(angle) * 8;
                const y = Math.sin(angle) * 8;
                if (i === 0) ctx.moveTo(x, y);
                else ctx.lineTo(x, y);
            }
            ctx.closePath();
            ctx.fill();
        } else {
            // 绘制骷髅
            ctx.beginPath();
            ctx.arc(0, 0, 5, 0, Math.PI * 2);
            ctx.fill();
            ctx.fillRect(-3, 3, 6, 2);
        }
        
        ctx.restore();
    }

    move(direction) {
        this.direction = direction;
        let newX = this.x;
        let newY = this.y;

        switch(direction) {
            case 'UP': newY -= this.speed; break;
            case 'DOWN': newY += this.speed; break;
            case 'LEFT': newX -= this.speed; break;
            case 'RIGHT': newX += this.speed; break;
        }

        // 检查边界碰撞
        if (newX >= 0 && newX <= config.canvasWidth - this.size &&
            newY >= 0 && newY <= config.canvasHeight - this.size) {
            
            // 检查墙壁碰撞
            let canMove = true;
            gameState.walls.forEach(wall => {
                if (this.checkWallCollision(newX, newY, wall)) {
                    canMove = false;
                }
            });

            if (canMove) {
                this.x = newX;
                this.y = newY;
                this.isMoving = true;
                this.moveTimer = 0;
            }
        }
    }

    checkWallCollision(newX, newY, wall) {
        return newX < wall.x + wall.width &&
               newX + this.size > wall.x &&
               newY < wall.y + wall.height &&
               newY + this.size > wall.y;
    }

    update() {
        if (!this.isPlayer) {
            // 敌人AI行为
            this.moveTimer++;
            if (this.moveTimer > 60) { // 每60帧改变一次方向
                const directions = ['UP', 'DOWN', 'LEFT', 'RIGHT'];
                this.move(directions[Math.floor(Math.random() * 4)]);
                this.moveTimer = 0;
            }

            // 随机射击
            if (Math.random() < 0.02) {
                this.shoot();
            }
        }
    }

    shoot() {
        const now = Date.now();
        if (now - this.lastShot >= this.shootDelay) {
            let bulletX = this.x + this.size / 2;
            let bulletY = this.y + this.size / 2;

            switch(this.direction) {
                case 'UP': bulletY = this.y; break;
                case 'DOWN': bulletY = this.y + this.size; break;
                case 'LEFT': bulletX = this.x; break;
                case 'RIGHT': bulletX = this.x + this.size; break;
            }

            gameState.bullets.push(new Bullet(bulletX, bulletY, this.direction, this.isPlayer));
            this.lastShot = now;
        }
    }

    hit() {
        this.health--;
        return this.health <= 0;
    }
}

// 子弹类
class Bullet {
    constructor(x, y, direction, isPlayerBullet) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.isPlayerBullet = isPlayerBullet;
        this.size = config.bulletSize;
        this.speed = config.bulletSpeed;
        this.power = isPlayerBullet ? 1 : 1; // 子弹威力
        this.trail = []; // 子弹轨迹
    }

    draw() {
        // 绘制子弹轨迹
        ctx.strokeStyle = this.isPlayerBullet ? 'rgba(255, 255, 0, 0.3)' : 'rgba(255, 0, 0, 0.3)';
        ctx.lineWidth = 2;
        ctx.beginPath();
        for (let i = 0; i < this.trail.length - 1; i++) {
            ctx.moveTo(this.trail[i].x, this.trail[i].y);
            ctx.lineTo(this.trail[i + 1].x, this.trail[i + 1].y);
        }
        ctx.stroke();

        // 绘制子弹主体
        const gradient = ctx.createRadialGradient(
            this.x, this.y, 0,
            this.x, this.y, this.size / 2
        );
        gradient.addColorStop(0, this.isPlayerBullet ? '#fff' : '#ff0');
        gradient.addColorStop(1, this.isPlayerBullet ? '#ff0' : '#f00');
        
        ctx.fillStyle = gradient;
        ctx.beginPath();
        ctx.arc(this.x, this.y, this.size / 2, 0, Math.PI * 2);
        ctx.fill();

        // 子弹光晕效果
        ctx.strokeStyle = this.isPlayerBullet ? '#ff0' : '#f00';
        ctx.lineWidth = 1;
        ctx.beginPath();
        ctx.arc(this.x, this.y, this.size / 2 + 2, 0, Math.PI * 2);
        ctx.stroke();
    }

    move() {
        // 保存当前位置到轨迹
        this.trail.push({ x: this.x, y: this.y });
        if (this.trail.length > 5) this.trail.shift();

        switch(this.direction) {
            case 'UP': this.y -= this.speed; break;
            case 'DOWN': this.y += this.speed; break;
            case 'LEFT': this.x -= this.speed; break;
            case 'RIGHT': this.x += this.speed; break;
        }
    }
}

// 墙壁类
class Wall {
    constructor(x, y, width, height, type = 'BRICK') {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.type = type;
        this.health = config.wallTypes[type].health;
        this.damage = 0;
    }

    draw() {
        // 绘制墙壁主体
        ctx.fillStyle = config.wallTypes[this.type].color;
        ctx.fillRect(this.x, this.y, this.width, this.height);

        // 根据墙壁类型添加纹理
        if (this.type === 'BRICK') {
            this.drawBrickTexture();
        } else if (this.type === 'STEEL') {
            this.drawSteelTexture();
        } else if (this.type === 'WATER') {
            this.drawWaterTexture();
        }

        // 如果墙壁受损，显示破损效果
        if (this.damage > 0) {
            this.drawDamage();
        }
    }

    drawBrickTexture() {
        ctx.strokeStyle = '#6B3E26';
        ctx.lineWidth = 1;
        // 水平线
        for (let y = this.y; y < this.y + this.height; y += 10) {
            ctx.beginPath();
            ctx.moveTo(this.x, y);
            ctx.lineTo(this.x + this.width, y);
            ctx.stroke();
        }
        // 垂直线
        for (let x = this.x; x < this.x + this.width; x += 20) {
            ctx.beginPath();
            ctx.moveTo(x, this.y);
            ctx.lineTo(x, this.y + this.height);
            ctx.stroke();
        }
    }

    drawSteelTexture() {
        ctx.strokeStyle = '#4A4A4A';
        ctx.lineWidth = 2;
        // 交叉线
        ctx.beginPath();
        ctx.moveTo(this.x, this.y);
        ctx.lineTo(this.x + this.width, this.y + this.height);
        ctx.moveTo(this.x + this.width, this.y);
        ctx.lineTo(this.x, this.y + this.height);
        ctx.stroke();
    }

    drawWaterTexture() {
        ctx.strokeStyle = 'rgba(255, 255, 255, 0.3)';
        ctx.lineWidth = 2;
        // 波浪线
        for (let y = this.y; y < this.y + this.height; y += 10) {
            ctx.beginPath();
            ctx.moveTo(this.x, y);
            for (let x = this.x; x < this.x + this.width; x += 10) {
                ctx.lineTo(x + 5, y + Math.sin(x * 0.1) * 3);
            }
            ctx.stroke();
        }
    }

    drawDamage() {
        ctx.fillStyle = 'rgba(0, 0, 0, 0.3)';
        for (let i = 0; i < this.damage; i++) {
            const x = this.x + Math.random() * this.width;
            const y = this.y + Math.random() * this.height;
            const size = Math.random() * 5 + 2;
            ctx.beginPath();
            ctx.arc(x, y, size, 0, Math.PI * 2);
            ctx.fill();
        }
    }

    hit() {
        if (this.type === 'WATER') return false;
        this.damage++;
        this.health--;
        return this.health <= 0;
    }
}

// 基地类
class Base {
    constructor(x, y) {
        this.x = x;
        this.y = y;
        this.size = config.tankSize;
        this.health = 100;
    }

    draw() {
        // 绘制基地主体
        ctx.fillStyle = '#0f0';
        ctx.fillRect(this.x, this.y, this.size, this.size);
        
        // 基地边框
        ctx.strokeStyle = '#fff';
        ctx.lineWidth = 3;
        ctx.strokeRect(this.x, this.y, this.size, this.size);
        
        // 基地内部结构
        ctx.fillStyle = '#0c0';
        ctx.fillRect(this.x + 5, this.y + 5, this.size - 10, this.size - 10);
        
        // 基地标志
        ctx.fillStyle = '#fff';
        ctx.beginPath();
        ctx.arc(this.x + this.size / 2, this.y + this.size / 2, this.size / 4, 0, Math.PI * 2);
        ctx.fill();
        
        // 基地天线
        ctx.strokeStyle = '#fff';
        ctx.lineWidth = 2;
        ctx.beginPath();
        ctx.moveTo(this.x + this.size / 2, this.y);
        ctx.lineTo(this.x + this.size / 2, this.y - 10);
        ctx.stroke();
        
        // 基地天线顶部
        ctx.beginPath();
        ctx.arc(this.x + this.size / 2, this.y - 10, 3, 0, Math.PI * 2);
        ctx.fill();
        
        // 基地防护罩效果
        ctx.strokeStyle = 'rgba(0, 255, 0, 0.3)';
        ctx.lineWidth = 2;
        ctx.beginPath();
        ctx.arc(this.x + this.size / 2, this.y + this.size / 2, this.size * 0.8, 0, Math.PI * 2);
        ctx.stroke();
    }
}

// 初始化游戏
function initGame() {
    // 创建玩家坦克
    gameState.player = new Tank(
        config.canvasWidth / 2 - config.tankSize / 2,
        config.canvasHeight - config.tankSize * 2,
        '#00f',
        true
    );

    // 创建基地
    gameState.base = new Base(
        config.canvasWidth / 2 - config.tankSize / 2,
        config.canvasHeight - config.tankSize
    );

    // 创建墙壁
    createWalls();

    // 重置游戏状态
    gameState.enemies = [];
    gameState.bullets = [];
    gameState.score = 0;
    gameState.lives = 3;
    gameState.level = 1;
    gameState.isGameOver = false;
    gameState.isPaused = false;

    // 更新UI
    updateUI();

    // 开始游戏循环
    if (gameState.gameLoop) clearInterval(gameState.gameLoop);
    gameState.gameLoop = setInterval(gameLoop, 1000 / 60);

    // 开始生成敌人
    if (gameState.enemySpawner) clearInterval(gameState.enemySpawner);
    gameState.enemySpawner = setInterval(spawnEnemy, config.enemySpawnInterval);
}

// 创建墙壁
function createWalls() {
    gameState.walls = [];
    
    // 创建边界墙
    const wallThickness = 20;
    
    // 上边界
    gameState.walls.push(new Wall(0, 0, config.canvasWidth, wallThickness, 'STEEL'));
    // 下边界
    gameState.walls.push(new Wall(0, config.canvasHeight - wallThickness, config.canvasWidth, wallThickness, 'STEEL'));
    // 左边界
    gameState.walls.push(new Wall(0, 0, wallThickness, config.canvasHeight, 'STEEL'));
    // 右边界
    gameState.walls.push(new Wall(config.canvasWidth - wallThickness, 0, wallThickness, config.canvasHeight, 'STEEL'));

    // 添加一些随机障碍物
    for (let i = 0; i < 15; i++) {
        const x = Math.random() * (config.canvasWidth - 100) + 50;
        const y = Math.random() * (config.canvasHeight - 200) + 50;
        const type = Math.random() < 0.7 ? 'BRICK' : (Math.random() < 0.5 ? 'STEEL' : 'WATER');
        gameState.walls.push(new Wall(x, y, 40, 40, type));
    }

    // 在基地周围添加保护墙
    const baseX = config.canvasWidth / 2 - config.tankSize / 2;
    const baseY = config.canvasHeight - config.tankSize;
    gameState.walls.push(new Wall(baseX - 40, baseY - 40, 40, 40, 'STEEL'));
    gameState.walls.push(new Wall(baseX + config.tankSize, baseY - 40, 40, 40, 'STEEL'));
    gameState.walls.push(new Wall(baseX - 40, baseY + config.tankSize, 40, 40, 'STEEL'));
    gameState.walls.push(new Wall(baseX + config.tankSize, baseY + config.tankSize, 40, 40, 'STEEL'));
}

// 生成敌人
function spawnEnemy() {
    if (gameState.enemies.length >= config.maxEnemies || gameState.isGameOver) return;

    const x = Math.random() * (config.canvasWidth - config.tankSize);
    const enemy = new Tank(x, 0, '#f00');
    enemy.speed = config.enemySpeed; // 使用当前难度下的速度
    gameState.enemies.push(enemy);
}

// 更新UI
function updateUI() {
    document.getElementById('score').innerText = `分数: ${gameState.score}`;
    document.getElementById('lives').innerText = `生命: ${gameState.lives}`;
    document.getElementById('level').innerText = `关卡: ${gameState.level}`;
}

// 修改碰撞检测函数
function checkCollision(obj1, obj2) {
    // 获取对象的实际碰撞区域
    const obj1Left = obj1.x;
    const obj1Right = obj1.x + obj1.size;
    const obj1Top = obj1.y;
    const obj1Bottom = obj1.y + obj1.size;

    const obj2Left = obj2.x;
    const obj2Right = obj2.x + obj2.size;
    const obj2Top = obj2.y;
    const obj2Bottom = obj2.y + obj2.size;

    // 检查是否有重叠
    return !(obj1Right < obj2Left || 
             obj1Left > obj2Right || 
             obj1Bottom < obj2Top || 
             obj1Top > obj2Bottom);
}

// 修改游戏主循环中的子弹碰撞检测部分
function gameLoop() {
    if (gameState.isPaused || gameState.isGameOver) return;

    // 清空画布
    ctx.clearRect(0, 0, config.canvasWidth, config.canvasHeight);

    // 绘制墙壁
    gameState.walls.forEach(wall => wall.draw());

    // 绘制基地
    gameState.base.draw();

    // 更新和绘制玩家
    gameState.player.update();
    gameState.player.draw();

    // 更新和绘制敌人
    gameState.enemies.forEach((enemy, index) => {
        enemy.update();
        enemy.draw();
    });

    // 更新和绘制子弹
    for (let i = gameState.bullets.length - 1; i >= 0; i--) {
        const bullet = gameState.bullets[i];
        bullet.move();
        bullet.draw();

        // 检查子弹是否击中墙壁
        let wallHit = false;
        for (let j = gameState.walls.length - 1; j >= 0; j--) {
            const wall = gameState.walls[j];
            if (checkCollision(bullet, wall)) {
                if (wall.hit()) {
                    gameState.walls.splice(j, 1);
                }
                createWallHitEffect(bullet.x, bullet.y);
                gameState.bullets.splice(i, 1);
                wallHit = true;
                break;
            }
        }
        if (wallHit) continue;

        // 检查子弹是否击中基地
        if (checkCollision(bullet, gameState.base)) {
            createExplosion(bullet.x, bullet.y);
            gameOver();
            return;
        }

        // 检查子弹是否击中坦克
        if (bullet.isPlayerBullet) {
            let enemyHit = false;
            for (let j = gameState.enemies.length - 1; j >= 0; j--) {
                const enemy = gameState.enemies[j];
                if (checkCollision(bullet, enemy)) {
                    createExplosion(enemy.x + enemy.size / 2, enemy.y + enemy.size / 2);
                    gameState.enemies.splice(j, 1);
                    gameState.bullets.splice(i, 1);
                    gameState.score += 100;
                    updateUI();
                    enemyHit = true;
                    break;
                }
            }
            if (enemyHit) continue;
        } else {
            if (checkCollision(bullet, gameState.player)) {
                createExplosion(bullet.x, bullet.y);
                if (gameState.player.hit()) {
                    createTankExplosion(gameState.player.x + gameState.player.size / 2, 
                                     gameState.player.y + gameState.player.size / 2);
                    gameState.lives--;
                    updateUI();
                    if (gameState.lives <= 0) {
                        gameOver();
                    } else {
                        gameState.player.x = config.canvasWidth / 2 - config.tankSize / 2;
                        gameState.player.y = config.canvasHeight - config.tankSize * 2;
                        gameState.player.health = 3;
                    }
                }
                gameState.bullets.splice(i, 1);
                continue;
            }
        }

        // 移除超出边界的子弹
        if (bullet.x < 0 || bullet.x > config.canvasWidth ||
            bullet.y < 0 || bullet.y > config.canvasHeight) {
            gameState.bullets.splice(i, 1);
        }
    }

    // 增加游戏难度
    if (gameState.score > 0 && gameState.score % 500 === 0) {
        increaseDifficulty();
    }
}

// 游戏结束
function gameOver() {
    gameState.isGameOver = true;
    clearInterval(gameState.gameLoop);
    clearInterval(gameState.enemySpawner);
    document.getElementById('restartBtn').style.display = 'inline-block';
    document.getElementById('startBtn').style.display = 'none';

    // 绘制游戏结束文字
    ctx.fillStyle = 'rgba(0, 0, 0, 0.7)';
    ctx.fillRect(0, 0, config.canvasWidth, config.canvasHeight);
    ctx.fillStyle = '#ff0';
    ctx.font = '48px Arial';
    ctx.textAlign = 'center';
    ctx.fillText('游戏结束', config.canvasWidth / 2, config.canvasHeight / 2);
    ctx.font = '24px Arial';
    ctx.fillText(`最终得分: ${gameState.score}`, config.canvasWidth / 2, config.canvasHeight / 2 + 40);
}

// 键盘控制
document.addEventListener('keydown', (e) => {
    if (gameState.isGameOver || gameState.isPaused) return;

    switch(e.key) {
        case 'ArrowUp':
            gameState.player.move('UP');
            break;
        case 'ArrowDown':
            gameState.player.move('DOWN');
            break;
        case 'ArrowLeft':
            gameState.player.move('LEFT');
            break;
        case 'ArrowRight':
            gameState.player.move('RIGHT');
            break;
        case ' ':
            gameState.player.shoot();
            break;
    }
});

// 按钮事件监听
document.getElementById('startBtn').addEventListener('click', () => {
    document.getElementById('startBtn').style.display = 'none';
    initGame();
});

document.getElementById('restartBtn').addEventListener('click', () => {
    document.getElementById('restartBtn').style.display = 'none';
    document.getElementById('startBtn').style.display = 'inline-block';
    initGame();
});

// 创建墙壁击中效果
function createWallHitEffect(x, y) {
    const particles = [];
    const particleCount = 15;
    
    for (let i = 0; i < particleCount; i++) {
        particles.push({
            x: x,
            y: y,
            vx: (Math.random() - 0.5) * 6,
            vy: (Math.random() - 0.5) * 6,
            life: 1,
            size: Math.random() * 3 + 1
        });
    }

    function drawWallHit() {
        particles.forEach((particle, index) => {
            particle.x += particle.vx;
            particle.y += particle.vy;
            particle.life -= 0.05;

            if (particle.life <= 0) {
                particles.splice(index, 1);
                return;
            }

            ctx.fillStyle = `rgba(139, 69, 19, ${particle.life})`;
            ctx.beginPath();
            ctx.arc(particle.x, particle.y, particle.size, 0, Math.PI * 2);
            ctx.fill();
        });

        if (particles.length > 0) {
            requestAnimationFrame(drawWallHit);
        }
    }

    drawWallHit();
}

// 创建子弹爆炸效果
function createExplosion(x, y) {
    const particles = [];
    const particleCount = 20;
    
    for (let i = 0; i < particleCount; i++) {
        particles.push({
            x: x,
            y: y,
            vx: (Math.random() - 0.5) * 8,
            vy: (Math.random() - 0.5) * 8,
            life: 1,
            size: Math.random() * 3 + 1
        });
    }

    function drawExplosion() {
        particles.forEach((particle, index) => {
            particle.x += particle.vx;
            particle.y += particle.vy;
            particle.life -= 0.05;

            if (particle.life <= 0) {
                particles.splice(index, 1);
                return;
            }

            // 使用更鲜艳的颜色
            const hue = Math.random() * 60;
            ctx.fillStyle = `hsla(${hue}, 100%, 50%, ${particle.life})`;
            ctx.beginPath();
            ctx.arc(particle.x, particle.y, particle.size, 0, Math.PI * 2);
            ctx.fill();

            // 添加光晕效果
            ctx.fillStyle = `hsla(${hue}, 100%, 50%, ${particle.life * 0.3})`;
            ctx.beginPath();
            ctx.arc(particle.x, particle.y, particle.size * 2, 0, Math.PI * 2);
            ctx.fill();
        });

        if (particles.length > 0) {
            requestAnimationFrame(drawExplosion);
        }
    }

    drawExplosion();
}

// 创建坦克爆炸效果
function createTankExplosion(x, y) {
    const particles = [];
    const particleCount = 40;
    
    for (let i = 0; i < particleCount; i++) {
        particles.push({
            x: x,
            y: y,
            vx: (Math.random() - 0.5) * 12,
            vy: (Math.random() - 0.5) * 12,
            life: 1,
            size: Math.random() * 5 + 2
        });
    }

    function drawTankExplosion() {
        particles.forEach((particle, index) => {
            particle.x += particle.vx;
            particle.y += particle.vy;
            particle.life -= 0.03;

            if (particle.life <= 0) {
                particles.splice(index, 1);
                return;
            }

            // 使用更鲜艳的颜色
            const hue = Math.random() * 60;
            ctx.fillStyle = `hsla(${hue}, 100%, 50%, ${particle.life})`;
            ctx.beginPath();
            ctx.arc(particle.x, particle.y, particle.size, 0, Math.PI * 2);
            ctx.fill();

            // 添加光晕效果
            ctx.fillStyle = `hsla(${hue}, 100%, 50%, ${particle.life * 0.3})`;
            ctx.beginPath();
            ctx.arc(particle.x, particle.y, particle.size * 3, 0, Math.PI * 2);
            ctx.fill();
        });

        if (particles.length > 0) {
            requestAnimationFrame(drawTankExplosion);
        }
    }

    drawTankExplosion();
}

// 增加游戏难度
function increaseDifficulty() {
    // 增加敌人速度
    config.enemySpeed = Math.min(
        config.enemySpeed + config.difficultyIncrease.speed,
        config.difficultyIncrease.maxSpeed
    );
    
    // 减少敌人生成间隔
    config.enemySpawnInterval = Math.max(
        config.enemySpawnInterval - config.difficultyIncrease.interval,
        1000
    );

    // 更新现有敌人的速度
    gameState.enemies.forEach(enemy => {
        enemy.speed = config.enemySpeed;
    });

    // 重新设置敌人生成器
    if (gameState.enemySpawner) {
        clearInterval(gameState.enemySpawner);
        gameState.enemySpawner = setInterval(spawnEnemy, config.enemySpawnInterval);
    }
} 