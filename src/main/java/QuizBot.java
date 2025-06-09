import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultArticle;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import participant.User;
import utils.PropertiesUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class QuizBot extends TelegramLongPollingBot {
    private final String BOT_TOKEN = PropertiesUtil.getBotToken();
    private final String BOT_USERNAME = PropertiesUtil.getBotUsername();
    private final String GAME_URL = PropertiesUtil.getGameUrl();
    private final Map<Long, User> users = new ConcurrentHashMap<>();
    private final static Logger logger = LogManager.getLogger(QuizBot.class);
    private final Set<Long> newUsers = new HashSet<>();

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            String username = update.getMessage().getFrom().getUserName() != null
                    ? update.getMessage().getFrom().getUserName()
                    : update.getMessage().getFrom().getFirstName();

            if (users.putIfAbsent(chatId, new User(username)) != null || !newUsers.add(chatId)) {
                logger.info("User already registered or interacted: {} with chatId: {}", username, chatId);
            } else {
                sendWelcomeMessage(chatId, username);
                logger.info("New user registered: {} with chatId: {}", username, chatId);
            }

            if (messageText.equals("/startquiz")) {
                sendWebAppButton(chatId);
                logger.info("User with chatId: {} started quiz", username);
            }
        } else if (update.hasInlineQuery()) {
            logger.info("Received inline query");
            handleInlineQuery(update);
        } else if (update.hasMessage() && update.getMessage().getWebAppData() != null) {
            Long chatId = update.getMessage().getChatId();
            String webAppData = null;
            try {
                if (update.getMessage().getWebAppData() != null) {
                    webAppData = update.getMessage().getWebAppData().getData();
                    logger.debug("WebAppData received: {}", webAppData);
                } else {
                    logger.warn("WebAppData not found for chatId: {}", chatId);
                    sendMessage(chatId, "Ошибка: Данные из Mini App не найдены.");
                    return;
                }

                int score = Integer.parseInt(webAppData.split("\"score\":")[1].replaceAll("[^0-9]", ""));
                users.get(chatId).setScore(score);
                logger.info("Quiz completed for chatId: {} with score: {}", chatId, score);
                sendMessage(chatId, "Квиз завершён! Ваш счёт: " + score);
            } catch (Exception e) {
                logger.error("Error processing webAppData: {}", e.getMessage(), e);
                sendMessage(chatId, "Ошибка обработки результатов: " + (webAppData != null ? webAppData : "Данные отсутствуют") + ". " + e.getMessage());
            }
        }
    }

    private void sendWelcomeMessage(Long chatId, String username) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(String.format(
                "Привет, %s! 👋\n\nДобро пожаловать в квиз-бот! Здесь ты можешь проверить свои знания в программировании и технологиях.\n" +
                        "- Открой квиз через кнопку ниже.\n" +
                        "- У тебя будет 15 секунд на каждый вопрос.\n" +
                        "- После завершения узнаешь свой счёт!\n\nНажми 'Открыть квиз', чтобы начать!",
                username
        ));
        try {
            execute(message);
            logger.info("Welcome message sent to chatId: {}", chatId);
        } catch (TelegramApiException e) {
            logger.error("Failed to send welcome message to chatId: {} - {}", chatId, e.getMessage(), e);
        }
    }

    private void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);
        try {
            execute(message);
            logger.info("Message: {} sent to chatId: {}", message, chatId);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendWebAppButton(Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText("Откройте квиз прямо здесь!");

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        KeyboardButton button = new KeyboardButton();
        button.setText("Открыть квиз");
        button.setWebApp(new WebAppInfo(GAME_URL));
        row.add(button);
        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
            logger.info("WebApp button sent to chatId: {}", chatId);
        } catch (TelegramApiException e) {
            logger.error("Failed to send WebApp button: {}", e.getMessage(), e);
        }
    }

    private void handleInlineQuery(Update update) {
        String query = update.getInlineQuery().getQuery();
        String queryId = update.getInlineQuery().getId();

        List<InlineQueryResult> results = new ArrayList<>();

        InlineQueryResultArticle articleResult = new InlineQueryResultArticle();
        articleResult.setId(UUID.randomUUID().toString());
        articleResult.setTitle("Играть в квиз!");
        articleResult.setDescription("Пройди квиз с вопросами за 15 секунд!");
        articleResult.setInputMessageContent(
                new InputTextMessageContent("Нажми, чтобы начать квиз! @VentionQuizUZBot")
        );
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("Открыть квиз");
        button.setWebApp(new WebAppInfo(GAME_URL)); // Указываем URL Mini App
        row.add(button);
        keyboard.add(row);
        inlineKeyboardMarkup.setKeyboard(keyboard);

        articleResult.setReplyMarkup(inlineKeyboardMarkup); // Устанавливаем InlineKeyboardMarkup
        results.add(articleResult);

        AnswerInlineQuery answerInlineQuery = new AnswerInlineQuery();
        answerInlineQuery.setInlineQueryId(queryId);
        answerInlineQuery.setResults(results);
        answerInlineQuery.setCacheTime(0); // Отключаем кэширование

        try {
            execute(answerInlineQuery);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}