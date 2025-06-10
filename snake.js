// 游戏配置
const config = {
    box: 20,
    canvasSize: 400,
    initialSpeed: 120,
    speedIncrease: 5,
    maxSpeed: 50
};

// 游戏状态
const gameState = {
    snake: [{ x: 8, y: 10 }],
    direction: 'RIGHT',
    nextDirection: 'RIGHT',
    food: null,
    score: 0,
    gameInterval: null,
    isGameOver: false,
    speed: config.initialSpeed
};

const canvas = document.getElementById('gameCanvas');
const ctx = canvas.getContext('2d');

// 初始化游戏
function initGame() {
    gameState.food = randomFood();
    gameState.snake = [{ x: 8, y: 10 }];
    gameState.direction = 'RIGHT';
    gameState.nextDirection = 'RIGHT';
    gameState.score = 0;
    gameState.isGameOver = false;
    gameState.speed = config.initialSpeed;
    document.getElementById('restartBtn').style.display = 'none';
    draw();
    if (gameState.gameInterval) clearInterval(gameState.gameInterval);
    gameState.gameInterval = setInterval(gameLoop, gameState.speed);
}

function randomFood() {
    let newFood;
    do {
        newFood = {
            x: Math.floor(Math.random() * (config.canvasSize / config.box)),
            y: Math.floor(Math.random() * (config.canvasSize / config.box))
        };
    } while (gameState.snake.some(seg => seg.x === newFood.x && seg.y === newFood.y));
    return newFood;
}

function drawSnake() {
    ctx.fillStyle = '#4caf50';
    gameState.snake.forEach((segment, i) => {
        ctx.fillRect(segment.x * config.box, segment.y * config.box, config.box, config.box);
        if (i === 0) {
            ctx.strokeStyle = '#fff';
            ctx.strokeRect(segment.x * config.box, segment.y * config.box, config.box, config.box);
        }
    });
}

function drawFood() {
    ctx.fillStyle = '#ff5722';
    ctx.fillRect(gameState.food.x * config.box, gameState.food.y * config.box, config.box, config.box);
}

function drawScore() {
    document.getElementById('score').innerText = `分数: ${gameState.score}`;
}

function checkCollision(head) {
    return (
        head.x < 0 || 
        head.x >= config.canvasSize / config.box ||
        head.y < 0 || 
        head.y >= config.canvasSize / config.box ||
        gameState.snake.some(seg => seg.x === head.x && seg.y === head.y)
    );
}

function moveSnake() {
    const head = { ...gameState.snake[0] };
    gameState.direction = gameState.nextDirection;
    
    switch (gameState.direction) {
        case 'LEFT': head.x -= 1; break;
        case 'UP': head.y -= 1; break;
        case 'RIGHT': head.x += 1; break;
        case 'DOWN': head.y += 1; break;
    }

    if (checkCollision(head)) {
        gameOver();
        return;
    }

    gameState.snake.unshift(head);

    if (head.x === gameState.food.x && head.y === gameState.food.y) {
        gameState.score++;
        gameState.food = randomFood();
        // 增加游戏速度
        if (gameState.speed > config.maxSpeed) {
            gameState.speed -= config.speedIncrease;
            clearInterval(gameState.gameInterval);
            gameState.gameInterval = setInterval(gameLoop, gameState.speed);
        }
    } else {
        gameState.snake.pop();
    }
}

function draw() {
    ctx.clearRect(0, 0, config.canvasSize, config.canvasSize);
    drawSnake();
    drawFood();
    drawScore();
}

function gameLoop() {
    moveSnake();
    draw();
}

document.addEventListener('keydown', e => {
    if (gameState.isGameOver) return;
    switch (e.key) {
        case 'ArrowLeft': 
            if (gameState.direction !== 'RIGHT') gameState.nextDirection = 'LEFT';
            break;
        case 'ArrowUp': 
            if (gameState.direction !== 'DOWN') gameState.nextDirection = 'UP';
            break;
        case 'ArrowRight': 
            if (gameState.direction !== 'LEFT') gameState.nextDirection = 'RIGHT';
            break;
        case 'ArrowDown': 
            if (gameState.direction !== 'UP') gameState.nextDirection = 'DOWN';
            break;
    }
});

document.getElementById('restartBtn').onclick = initGame;

function gameOver() {
    clearInterval(gameState.gameInterval);
    gameState.isGameOver = true;
    document.getElementById('restartBtn').style.display = 'inline-block';
    ctx.fillStyle = 'rgba(0,0,0,0.5)';
    ctx.fillRect(0, config.canvasSize / 2 - 30, config.canvasSize, 60);
    ctx.fillStyle = '#fff';
    ctx.font = '28px Arial';
    ctx.textAlign = 'center';
    ctx.fillText('游戏结束', config.canvasSize / 2, config.canvasSize / 2);
}

// 开始游戏
initGame();
