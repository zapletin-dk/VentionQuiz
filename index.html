<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quiz Game</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            height: 100vh;
            margin: 0;
            background-color: #f0f0f0;
        }
        #quiz-container {
            text-align: center;
            background: white;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        #question {
            font-size: 1.5em;
            margin-bottom: 20px;
        }
        #options {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 10px;
        }
        button {
            padding: 10px;
            font-size: 1em;
            cursor: pointer;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 5px;
        }
        button:hover {
            background-color: #0056b3;
        }
        #timer {
            margin-top: 20px;
            font-size: 1.2em;
            color: red;
        }
        #result {
            margin-top: 20px;
            font-size: 1.2em;
        }
    </style>
</head>
<body>
    <div id="quiz-container">
        <div id="question"></div>
        <div id="options"></div>
        <div id="timer">Осталось: 15 секунд</div>
        <div id="result"></div>
    </div>
    <script>
        const questions = [
            {
                question: "Какой язык программирования был создан первым?",
                options: ["Java", "C", "Python", "JavaScript"],
                correctAnswer: "C"
            },
            {
                question: "Что такое JVM?",
                options: ["Java Virtual Machine", "JavaScript Virtual Machine", "Just Virtual Machine", "Java Version Manager"],
                correctAnswer: "Java Virtual Machine"
            }
        ];

        let currentQuestionIndex = 0;
        let score = 0;
        let timeLeft = 15;
        let timer;

        function startQuiz() {
            showQuestion();
        }

        function showQuestion() {
            const question = questions[currentQuestionIndex];
            document.getElementById('question').textContent = `Вопрос ${currentQuestionIndex + 1}: ${question.question}`;
            const optionsDiv = document.getElementById('options');
            optionsDiv.innerHTML = '';
            question.options.forEach(option => {
                const button = document.createElement('button');
                button.textContent = option;
                button.onclick = () => checkAnswer(option);
                optionsDiv.appendChild(button);
            });
            document.getElementById('result').textContent = '';
            timeLeft = 15;
            document.getElementById('timer').textContent = `Осталось: ${timeLeft} секунд`;
            startTimer();
        }

        function startTimer() {
            clearInterval(timer);
            timer = setInterval(() => {
                timeLeft--;
                document.getElementById('timer').textContent = `Осталось: ${timeLeft} секунд`;
                if (timeLeft <= 0) {
                    clearInterval(timer);
                    checkAnswer(null);
                }
            }, 1000);
        }

        function checkAnswer(selected) {
            clearInterval(timer);
            const question = questions[currentQuestionIndex];
            const resultDiv = document.getElementById('result');
            if (selected && selected === question.correctAnswer) {
                score++;
                resultDiv.textContent = `Правильно! Текущий счёт: ${score}`;
            } else if (selected) {
                resultDiv.textContent = `Неправильно. Правильный ответ: ${question.correctAnswer}`;
            } else {
                resultDiv.textContent = `Время вышло! Правильный ответ: ${question.correctAnswer}`;
            }
            setTimeout(() => {
                currentQuestionIndex++;
                if (currentQuestionIndex < questions.length) {
                    showQuestion();
                } else {
                    endQuiz();
                }
            }, 2000);
        }

        function endQuiz() {
            document.getElementById('question').textContent = 'Квиз завершён!';
            document.getElementById('options').innerHTML = '';
            document.getElementById('timer').textContent = '';
            document.getElementById('result').textContent = `Ваш итоговый счёт: ${score}`;
            if (window.Telegram && window.Telegram.WebApp) {
                window.Telegram.WebApp.sendData(JSON.stringify({ score }));
            }
        }

        startQuiz();
    </script>
</body>
</html>
