import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class RudeChatBotExe extends JFrame {

    private JTextArea inputArea;
    private JTextArea analysisArea;
    private JTextArea replyArea;
    private JTextArea memoryArea;
    private DefaultListModel<String> intentModel;

    private final BotBrain botBrain = new BotBrain();

    public RudeChatBotExe() {
        setupWindow();
        setupUI();
        seedExample();
    }

    private void setupWindow() {
        setTitle("RudeChat Bot.exe");
        setSize(1200, 760);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void setupUI() {
        JPanel root = new JPanel(new BorderLayout(14, 14));
        root.setBackground(new Color(13, 13, 18));
        root.setBorder(new EmptyBorder(18, 18, 18, 18));
        setContentPane(root);

        JLabel title = new JLabel("RudeChat Bot.exe");
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setForeground(Color.WHITE);
        root.add(title, BorderLayout.NORTH);

        JPanel main = new JPanel(new GridLayout(1, 3, 14, 14));
        main.setOpaque(false);
        root.add(main, BorderLayout.CENTER);

        main.add(createInputPanel());
        main.add(createIntentPanel());
        main.add(createReplyPanel());

        root.add(createFooterPanel(), BorderLayout.SOUTH);
    }

    private JPanel createInputPanel() {
        JPanel panel = cardPanel();
        panel.setLayout(new BorderLayout(10, 10));

        panel.add(sectionLabel("Incoming User Message"), BorderLayout.NORTH);

        inputArea = new JTextArea();
        styleTextArea(inputArea);
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);

        panel.add(new JScrollPane(inputArea), BorderLayout.CENTER);

        JPanel buttons = new JPanel(new GridLayout(2, 2, 8, 8));
        buttons.setOpaque(false);

        JButton detectButton = button("Detect + Reply");
        detectButton.addActionListener(e -> detectAndReply());

        JButton randomiseButton = button("Randomise Reply");
        randomiseButton.addActionListener(e -> randomiseReply());

        JButton simulateButton = button("Simulate");
        simulateButton.addActionListener(e -> simulateMessage());

        JButton clearButton = button("Clear + Reset");
        clearButton.addActionListener(e -> {
            inputArea.setText("");
            analysisArea.setText("");
            replyArea.setText("");
            memoryArea.setText("");
            intentModel.clear();
            botBrain.resetConversation();
        });

        buttons.add(detectButton);
        buttons.add(randomiseButton);
        buttons.add(simulateButton);
        buttons.add(clearButton);

        panel.add(buttons, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createIntentPanel() {
        JPanel panel = cardPanel();
        panel.setLayout(new BorderLayout(10, 10));

        panel.add(sectionLabel("Detected Intent + Analysis"), BorderLayout.NORTH);

        intentModel = new DefaultListModel<>();

        JList<String> intentList = new JList<>(intentModel);
        intentList.setBackground(new Color(25, 25, 34));
        intentList.setForeground(Color.WHITE);
        intentList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        intentList.setSelectionBackground(new Color(85, 70, 170));
        intentList.setFixedCellHeight(34);

        analysisArea = new JTextArea();
        styleTextArea(analysisArea);
        analysisArea.setEditable(false);
        analysisArea.setLineWrap(true);
        analysisArea.setWrapStyleWord(true);

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(intentList),
                new JScrollPane(analysisArea)
        );

        splitPane.setResizeWeight(0.34);
        splitPane.setBorder(null);
        splitPane.setDividerSize(7);

        panel.add(splitPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createReplyPanel() {
        JPanel panel = cardPanel();
        panel.setLayout(new BorderLayout(10, 10));

        panel.add(sectionLabel("Random Bot Reply + Memory"), BorderLayout.NORTH);

        replyArea = new JTextArea();
        styleTextArea(replyArea);
        replyArea.setEditable(false);
        replyArea.setLineWrap(true);
        replyArea.setWrapStyleWord(true);
        replyArea.setFont(new Font("Segoe UI", Font.BOLD, 16));

        memoryArea = new JTextArea();
        styleTextArea(memoryArea);
        memoryArea.setEditable(false);
        memoryArea.setLineWrap(true);
        memoryArea.setWrapStyleWord(true);
        memoryArea.setFont(new Font("Consolas", Font.PLAIN, 12));

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(replyArea),
                new JScrollPane(memoryArea)
        );

        splitPane.setResizeWeight(0.62);
        splitPane.setBorder(null);
        splitPane.setDividerSize(7);

        panel.add(splitPane, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new GridLayout(1, 2, 8, 8));
        buttons.setOpaque(false);

        JButton copyReplyButton = button("Copy Reply");
        copyReplyButton.addActionListener(e -> copyToClipboard(replyArea.getText()));

        JButton copyAnalysisButton = button("Copy Analysis");
        copyAnalysisButton.addActionListener(e -> copyToClipboard(analysisArea.getText()));

        buttons.add(copyReplyButton);
        buttons.add(copyAnalysisButton);

        panel.add(buttons, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createFooterPanel() {
        JPanel panel = cardPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setPreferredSize(new Dimension(1200, 84));

        JTextArea info = new JTextArea(
                "Local rude chatbot template. Detects greetings, random openers, swearing, bugs, support, applications, payments, Discord links, events, thanks, slang, common words, mixed nonsense, and follow-up context. " +
                "Replies are intentionally harsh, but protected-class slurs are not used."
        );

        styleTextArea(info);
        info.setEditable(false);

        panel.add(info, BorderLayout.CENTER);

        return panel;
    }

    private JPanel cardPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(20, 20, 28));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 60, 80), 1),
                new EmptyBorder(14, 14, 14, 14)
        ));
        return panel;
    }

    private JLabel sectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        return label;
    }

    private JButton button(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(55, 55, 78));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(95, 95, 125), 1));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void styleTextArea(JTextArea area) {
        area.setBackground(new Color(25, 25, 34));
        area.setForeground(new Color(230, 230, 240));
        area.setCaretColor(Color.WHITE);
        area.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        area.setBorder(new EmptyBorder(10, 10, 10, 10));
    }

    private void seedExample() {
        inputArea.setText("yo quick question this menu is broken and it wont work wtf");
        detectAndReply();
    }

    private void detectAndReply() {
        String message = inputArea.getText().trim();

        intentModel.clear();
        analysisArea.setText("");
        replyArea.setText("");

        if (message.isEmpty()) {
            analysisArea.setText("No message entered.");
            replyArea.setText("Type something first, genius.");
            memoryArea.setText(botBrain.getMemoryText());
            return;
        }

        DetectionReport report = botBrain.detect(message);
        String reply = botBrain.generateReply(report);

        for (DetectedIntent intent : report.intents) {
            intentModel.addElement(intent.name + " | " + intent.confidence + "%");
        }

        analysisArea.setText(report.toReadableText());
        replyArea.setText(reply);
        memoryArea.setText(botBrain.getMemoryText());
    }

    private void randomiseReply() {
        String message = inputArea.getText().trim();

        if (message.isEmpty()) {
            replyArea.setText("Type something first, genius.");
            return;
        }

        DetectionReport report = botBrain.detect(message);
        String reply = botBrain.generateReplyWithoutMemoryUpdate(report);

        replyArea.setText(reply);
    }

    private void simulateMessage() {
        String[] examples = {
                "hi",
                "hello can anyone help me",
                "quick question",
                "i need staff",
                "i have a problem",
                "this is broken",
                "this shit wont work",
                "how do i apply?",
                "where is the discord link?",
                "how much does this cost?",
                "thanks mate",
                "yo bro this server is laggy",
                "i want to report a bug",
                "can someone help me with my application?",
                "i am confused what do i do",
                "random fucking word idiot idk what that one means",
                "banana microwave purple chair",
                "bro wtf is going on",
                "this dumb menu crashed again",
                "when is the next event?",
                "fuck mate hi",
                "yo wtf hello",
                "bro hi idiot",
                "ok",
                "still broken",
                "it crashes when i press open",
                "application pending no reply",
                "refund this shit",
                "discord invite expired"
        };

        inputArea.setText(examples[new Random().nextInt(examples.length)]);
        detectAndReply();
    }

    private void copyToClipboard(String text) {
        Toolkit.getDefaultToolkit()
                .getSystemClipboard()
                .setContents(new StringSelection(text == null ? "" : text), null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RudeChatBotExe app = new RudeChatBotExe();
            app.setVisible(true);
        });
    }

    static class BotBrain {

        private final Random random = new Random();
        private final Map<String, List<String>> keywordGroups = new LinkedHashMap<>();
        private final Map<String, List<String>> replyGroups = new LinkedHashMap<>();

        private String lastTopIntent = "";
        private String lastBotReply = "";
        private boolean awaitingFollowUp = false;
        private int conversationTurns = 0;
        private final List<String> conversationMemory = new ArrayList<>();

        BotBrain() {
            loadKeywordGroups();
            loadReplyGroups();
        }

        DetectionReport detect(String rawMessage) {
            String cleaned = clean(rawMessage);
            DetectionReport report = new DetectionReport(rawMessage, cleaned);

            addIntentIfMatched(report, "Greeting", cleaned, keywordGroups.get("Greeting"), 98);
            addIntentIfMatched(report, "Random Opener", cleaned, keywordGroups.get("Random Opener"), 94);
            addIntentIfMatched(report, "Swearing / Frustration", cleaned, keywordGroups.get("Swearing"), 96);
            addIntentIfMatched(report, "Bug / Error", cleaned, keywordGroups.get("Bug"), 92);
            addIntentIfMatched(report, "Application / Join", cleaned, keywordGroups.get("Application"), 90);
            addIntentIfMatched(report, "Support Request", cleaned, keywordGroups.get("Support"), 88);
            addIntentIfMatched(report, "Payment / Price", cleaned, keywordGroups.get("Payment"), 86);
            addIntentIfMatched(report, "Discord / Link", cleaned, keywordGroups.get("Discord"), 84);
            addIntentIfMatched(report, "Event / Schedule", cleaned, keywordGroups.get("Event"), 82);
            addIntentIfMatched(report, "Thanks / Appreciation", cleaned, keywordGroups.get("Thanks"), 80);
            addIntentIfMatched(report, "Slang / Casual", cleaned, keywordGroups.get("Slang"), 72);
            addIntentIfMatched(report, "Common General", cleaned, keywordGroups.get("Common"), 65);

            addComboIntents(report);

            if (report.intents.isEmpty()) {
                report.intents.add(new DetectedIntent("Unknown / Fallback", 30, new ArrayList<>()));
            }

            report.intents.sort((a, b) -> Integer.compare(b.confidence, a.confidence));

            return report;
        }

        String generateReply(DetectionReport report) {
            if (report == null || report.intents.isEmpty()) {
                return randomReply("Unknown / Fallback");
            }

            DetectedIntent topIntent = report.intents.get(0);
            String replyKey = topIntent.name;

            if (conversationTurns > 0) {
                replyKey = selectFollowUpReplyKey(report, topIntent.name);
            }

            String reply = randomReply(replyKey);

            lastTopIntent = topIntent.name;
            lastBotReply = reply;
            awaitingFollowUp = shouldAwaitFollowUp(topIntent.name, replyKey);
            conversationTurns++;

            conversationMemory.add("User: " + report.rawMessage);
            conversationMemory.add("Bot: " + reply);

            while (conversationMemory.size() > 20) {
                conversationMemory.remove(0);
            }

            return reply;
        }

        String generateReplyWithoutMemoryUpdate(DetectionReport report) {
            if (report == null || report.intents.isEmpty()) {
                return randomReply("Unknown / Fallback");
            }

            DetectedIntent topIntent = report.intents.get(0);
            String replyKey = topIntent.name;

            if (conversationTurns > 0) {
                replyKey = selectFollowUpReplyKey(report, topIntent.name);
            }

            return randomReply(replyKey);
        }

        private String selectFollowUpReplyKey(DetectionReport report, String currentTopIntent) {
            boolean isUnknown = report.hasIntent("Unknown / Fallback");
            boolean isGreeting = report.hasIntent("Greeting");
            boolean isRandomOpener = report.hasIntent("Random Opener");
            boolean isSwearing = report.hasIntent("Swearing / Frustration");
            boolean isBug = report.hasIntent("Bug / Error");
            boolean isSupport = report.hasIntent("Support Request");
            boolean isApplication = report.hasIntent("Application / Join");
            boolean isPayment = report.hasIntent("Payment / Price");
            boolean isDiscord = report.hasIntent("Discord / Link");
            boolean isEvent = report.hasIntent("Event / Schedule");
            boolean isSlang = report.hasIntent("Slang / Casual");
            boolean isCommon = report.hasIntent("Common General");

            boolean vagueMessage =
                    isGreeting ||
                    isRandomOpener ||
                    isSlang ||
                    isCommon ||
                    isUnknown;

            if (awaitingFollowUp) {
                if (isSwearing && vagueMessage && !isBug && !isSupport && !isApplication && !isPayment) {
                    return "FollowUp Angry But Still Vague";
                }

                if (isUnknown) {
                    return "FollowUp Still Useless";
                }

                if (isBug) {
                    return "FollowUp Bug Details";
                }

                if (isSupport) {
                    return "FollowUp Support Details";
                }

                if (isApplication) {
                    return "FollowUp Application Details";
                }

                if (isPayment) {
                    return "FollowUp Payment Details";
                }

                if (isDiscord) {
                    return "FollowUp Discord Details";
                }

                if (isEvent) {
                    return "FollowUp Event Details";
                }

                if (vagueMessage) {
                    return "FollowUp Missing Details";
                }
            }

            if (lastTopIntent.equals("Greeting") && isGreeting) {
                return "Repeated Greeting";
            }

            if (lastTopIntent.equals("Unknown / Fallback") && isUnknown) {
                return "Repeated Unknown";
            }

            if (lastTopIntent.equals("Swearing / Frustration") && isSwearing && vagueMessage) {
                return "Repeated Swearing";
            }

            return currentTopIntent;
        }

        private boolean shouldAwaitFollowUp(String topIntent, String replyKey) {
            Set<String> noFollowUpNeeded = Set.of(
                    "Thanks / Appreciation"
            );

            return !noFollowUpNeeded.contains(topIntent);
        }

        void resetConversation() {
            lastTopIntent = "";
            lastBotReply = "";
            awaitingFollowUp = false;
            conversationTurns = 0;
            conversationMemory.clear();
        }

        String getMemoryText() {
            StringBuilder builder = new StringBuilder();

            builder.append("Conversation Turns: ").append(conversationTurns).append("\n");
            builder.append("Last Intent: ").append(lastTopIntent.isEmpty() ? "None" : lastTopIntent).append("\n");
            builder.append("Awaiting Follow-Up: ").append(awaitingFollowUp).append("\n\n");

            if (conversationMemory.isEmpty()) {
                builder.append("No conversation memory yet.");
                return builder.toString();
            }

            builder.append("Recent Memory:\n");

            for (String line : conversationMemory) {
                builder.append("- ").append(line).append("\n");
            }

            return builder.toString();
        }

        private String randomReply(String intentName) {
            List<String> replies = replyGroups.get(intentName);

            if (replies == null || replies.isEmpty()) {
                replies = replyGroups.get("Unknown / Fallback");
            }

            return replies.get(random.nextInt(replies.size()));
        }

        private void addComboIntents(DetectionReport report) {
            boolean hasGreeting = report.hasIntent("Greeting");
            boolean hasRandomOpener = report.hasIntent("Random Opener");
            boolean hasSwearing = report.hasIntent("Swearing / Frustration");
            boolean hasBug = report.hasIntent("Bug / Error");
            boolean hasSupport = report.hasIntent("Support Request");
            boolean hasApplication = report.hasIntent("Application / Join");
            boolean hasPayment = report.hasIntent("Payment / Price");
            boolean hasSlang = report.hasIntent("Slang / Casual");
            boolean hasCommon = report.hasIntent("Common General");

            int usefulIntentCount = 0;

            if (hasGreeting) usefulIntentCount++;
            if (hasRandomOpener) usefulIntentCount++;
            if (hasSwearing) usefulIntentCount++;
            if (hasBug) usefulIntentCount++;
            if (hasSupport) usefulIntentCount++;
            if (hasApplication) usefulIntentCount++;
            if (hasPayment) usefulIntentCount++;
            if (hasSlang) usefulIntentCount++;
            if (hasCommon) usefulIntentCount++;

            if (
                    usefulIntentCount >= 3 &&
                    !hasBug &&
                    !hasSupport &&
                    !hasApplication &&
                    !hasPayment
            ) {
                report.intents.add(new DetectedIntent(
                        "Mixed / Confused Message",
                        100,
                        Arrays.asList("multiple random intents with no clear request")
                ));
            }

            if (hasGreeting && hasSwearing && hasSlang) {
                report.intents.add(new DetectedIntent(
                        "Aggressive Nonsense Greeting",
                        100,
                        Arrays.asList("greeting + swearing + slang")
                ));
            }

            if (hasSwearing && hasBug) {
                report.intents.add(new DetectedIntent(
                        "Angry Bug Report",
                        99,
                        Arrays.asList("swearing + bug/error")
                ));
            }

            if (hasSwearing && hasSupport) {
                report.intents.add(new DetectedIntent(
                        "Angry Support Request",
                        97,
                        Arrays.asList("swearing + support")
                ));
            }

            if (hasApplication && hasSupport) {
                report.intents.add(new DetectedIntent(
                        "Application Support",
                        95,
                        Arrays.asList("application + support")
                ));
            }

            if (hasSwearing && hasPayment) {
                report.intents.add(new DetectedIntent(
                        "Angry Payment Question",
                        94,
                        Arrays.asList("swearing + payment")
                ));
            }
        }

        private void addIntentIfMatched(
                DetectionReport report,
                String intentName,
                String cleanedMessage,
                List<String> keywords,
                int baseConfidence
        ) {
            List<String> matched = findMatches(cleanedMessage, keywords);

            if (matched.isEmpty()) {
                return;
            }

            int confidence = Math.min(99, baseConfidence + matched.size() * 2);

            report.intents.add(new DetectedIntent(intentName, confidence, matched));
        }

        private List<String> findMatches(String cleanedMessage, List<String> keywords) {
            List<String> matched = new ArrayList<>();

            if (keywords == null) {
                return matched;
            }

            for (String keyword : keywords) {
                String cleanedKeyword = clean(keyword);

                if (cleanedKeyword.isEmpty()) {
                    continue;
                }

                if (containsPhrase(cleanedMessage, cleanedKeyword)) {
                    matched.add(keyword);
                }
            }

            return matched;
        }

        private boolean containsPhrase(String cleanedMessage, String cleanedKeyword) {
            String paddedMessage = " " + cleanedMessage + " ";
            String paddedKeyword = " " + cleanedKeyword + " ";

            return paddedMessage.contains(paddedKeyword);
        }

        private String clean(String text) {
            if (text == null) {
                return "";
            }

            return text.toLowerCase()
                    .replace("’", "'")
                    .replaceAll("[^a-z0-9' ]", " ")
                    .replaceAll("\\s+", " ")
                    .trim();
        }

        private void loadKeywordGroups() {
            keywordGroups.put("Greeting", Arrays.asList(
                    "hi", "hello", "hey", "yo", "sup", "wassup", "whats up", "what's up", "wsg",
                    "hiya", "ello", "heya", "howdy", "morning", "good morning", "afternoon",
                    "good afternoon", "evening", "good evening", "night", "good night", "gm", "gn",
                    "anyone here", "any staff", "staff here", "is anyone here", "anybody here",
                    "can anyone help", "hello bot", "hi bot", "hey bot", "you there", "are you there"
            ));

            keywordGroups.put("Random Opener", Arrays.asList(
                    "i have a question", "quick question", "can i ask something", "i need something",
                    "i need help", "i need support", "i need staff", "i need a mod", "i need an admin",
                    "someone help", "can someone help", "anyone able to help", "i am confused",
                    "im confused", "i dont know what to do", "i don't know what to do", "not sure what to do",
                    "this is confusing", "i have an issue", "i have a problem", "something is wrong",
                    "something broke", "this isnt working", "this isn't working", "it is not working",
                    "it wont work", "it won't work", "i found a bug", "i think i found a bug",
                    "i need to report something", "i want to report something", "i want to ask about something",
                    "i need information", "i need more info", "i want to know something", "i have a suggestion",
                    "i have an idea", "i want to suggest something", "can you explain something",
                    "can you tell me something", "before i ask", "random question", "serious question",
                    "small question", "probably dumb question", "might be a stupid question",
                    "not sure if this is allowed", "is this allowed", "where do i go",
                    "who do i speak to", "who can help me", "what do i do", "what happens now",
                    "i need guidance", "point me in the right direction"
            ));

            keywordGroups.put("Swearing", Arrays.asList(
                    "fuck", "fucking", "fucked", "fucker", "shit", "shitty", "bullshit",
                    "bastard", "ass", "arse", "asshole", "dick", "prick", "twat", "wanker",
                    "cunt", "crap", "piss", "pissed", "damn", "goddamn", "hell", "wtf",
                    "stfu", "ffs", "idiot", "dumbass", "moron"
            ));

            keywordGroups.put("Bug", Arrays.asList(
                    "bug", "error", "glitch", "broken", "crash", "crashed", "freezing", "stuck",
                    "not working", "doesn't work", "doesnt work", "wont load", "won't load",
                    "failed", "timeout", "black screen", "blank screen", "cant open", "can't open",
                    "menu broken", "button broken", "system broken", "wont work", "won't work",
                    "laggy", "buggy", "crashes", "crashing", "does not work"
            ));

            keywordGroups.put("Application", Arrays.asList(
                    "apply", "application", "join", "form", "recruit", "recruitment", "tryout",
                    "trial", "accepted", "denied", "pending", "review", "role", "rank", "team",
                    "member", "staff application", "player application", "whitelist", "interview"
            ));

            keywordGroups.put("Support", Arrays.asList(
                    "support", "ticket", "help", "problem", "issue", "case", "request", "assistance",
                    "staff", "admin", "moderator", "manager", "owner", "developer", "dev", "team",
                    "review", "response", "reply", "update", "status"
            ));

            keywordGroups.put("Payment", Arrays.asList(
                    "price", "cost", "payment", "pay", "paid", "buy", "purchase", "refund",
                    "invoice", "subscription", "monthly", "lifetime", "package", "plan",
                    "cheap", "expensive", "discount", "deal", "offer"
            ));

            keywordGroups.put("Discord", Arrays.asList(
                    "discord", "invite", "link", "server link", "discord link", "join discord",
                    "server invite", "invite link", "expired invite", "invite expired"
            ));

            keywordGroups.put("Event", Arrays.asList(
                    "event", "tournament", "match", "scrim", "time", "schedule", "when", "date",
                    "fixture", "game night", "trial", "tryout"
            ));

            keywordGroups.put("Thanks", Arrays.asList(
                    "thanks", "thank you", "cheers", "appreciate", "ty", "thx", "nice one",
                    "legend", "thank u"
            ));

            keywordGroups.put("Slang", Arrays.asList(
                    "yo", "bro", "mate", "man", "fr", "ngl", "idk", "idek", "wsg", "sup",
                    "pls", "plz", "ty", "thx", "np", "lol", "lmao", "bruh", "nah",
                    "yeah", "yep", "nope", "alr", "ok", "okay"
            ));

            keywordGroups.put("Common", Arrays.asList(
                    "help", "issue", "problem", "question", "support", "staff", "admin", "mod",
                    "server", "bot", "link", "info", "details", "form", "apply", "application",
                    "join", "ticket", "report", "bug", "error", "broken", "fix", "working",
                    "not working", "confused", "stuck", "where", "what", "why", "how", "when",
                    "who", "can", "could", "need", "want", "trying", "unable", "cant", "can't",
                    "wont", "won't", "doesnt", "doesn't", "please", "thanks", "thank you", "cheers"
            ));
        }

        private void loadReplyGroups() {
            replyGroups.put("Greeting", Arrays.asList(
                    "Yeah, hello. Get to the point.",
                    "Hi. Try saying something useful now.",
                    "Hello. Congratulations, you found the chat box.",
                    "Yo. What do you need before this gets painful?",
                    "Hi. Speak clearly and do not make this weird.",
                    "Hello. I am here. Sadly, so are you.",
                    "Hey. What disaster are we dealing with today?",
                    "Yes, I exist. What do you want?"
            ));

            replyGroups.put("Random Opener", Arrays.asList(
                    "Go on then. Ask the question instead of hovering around it.",
                    "You opened with suspense and zero substance. Explain.",
                    "Brilliant intro. Now provide the actual issue.",
                    "I need details, not a dramatic loading screen.",
                    "Say the useful part before I lose interest.",
                    "Ask properly. I am not decoding cave paintings.",
                    "Start with what happened, what you need, and why this is my problem.",
                    "That was vague. Try again with actual information."
            ));

            replyGroups.put("Swearing / Frustration", Arrays.asList(
                    "Strong language, weak explanation. What actually happened?",
                    "Swearing detected. Useful information still missing.",
                    "Great, you are angry. Now explain the issue like a functioning adult.",
                    "That was emotional. Now give me the facts.",
                    "Rage noted. Evidence still required.",
                    "You can swear all you want, but the problem is still not explained.",
                    "Brilliant tantrum. Now describe the issue properly.",
                    "Calm down and type the useful part."
            ));

            replyGroups.put("Bug / Error", Arrays.asList(
                    "If it is broken, explain how. I am not psychic.",
                    "Send what you clicked, what happened, and what error showed. Basic stuff.",
                    "Bug report without details is just whining with extra steps.",
                    "Tell me the exact error before calling it broken.",
                    "What broke, when did it break, and can you reproduce it?",
                    "Screenshot, steps, error. Do that instead of just saying 'broken'.",
                    "A bug report needs details. Try acting like you want it fixed.",
                    "Explain the issue properly or enjoy the bug forever."
            ));

            replyGroups.put("Angry Bug Report", Arrays.asList(
                    "I get it, it is broken. Now stop raging and send the error, steps, and screenshot.",
                    "Angry bug report detected. Still need actual evidence, genius.",
                    "Swearing plus 'broken' is not a bug report. Give me steps to reproduce it.",
                    "Cool meltdown. Now tell me what clicked, what failed, and what showed on screen.",
                    "If you want it fixed, send facts. If you want to cry, continue as you were.",
                    "Broken how? Your explanation has the depth of a puddle.",
                    "Send the error properly before blaming the whole system.",
                    "You found a bug. Great. Now report it like you have used a computer before."
            ));

            replyGroups.put("Support Request", Arrays.asList(
                    "Explain the issue clearly and I might actually be able to help.",
                    "Support request detected. Details required. Shocking concept.",
                    "Tell me what happened, what you expected, and what actually happened.",
                    "If you need help, provide context instead of making me guess.",
                    "Start with the problem, not your life story.",
                    "Send the issue clearly. Half-sentences are useless.",
                    "I can help once you stop being vague.",
                    "Details first. Complaining second."
            ));

            replyGroups.put("Angry Support Request", Arrays.asList(
                    "You are clearly annoyed. Good for you. Now explain the problem properly.",
                    "Angry support request detected. Send details, not keyboard violence.",
                    "Being mad is not a support ticket. Explain what happened.",
                    "I can help if you stop typing like the keyboard owes you money.",
                    "Send the facts. Keep the tantrum optional.",
                    "You want support, not therapy. Describe the actual issue.",
                    "Calm the rage and provide the error or context.",
                    "Frustration noted. Now use words that solve something."
            ));

            replyGroups.put("Application / Join", Arrays.asList(
                    "If you want to join, fill in the application properly. Do not submit lazy nonsense.",
                    "Application detected. Use the form and answer like you actually want to be accepted.",
                    "You need to apply through the correct form. This is not difficult.",
                    "Join request detected. Read the requirements before embarrassing yourself.",
                    "Fill out the application and wait for review. Spamming will not make you special.",
                    "Applications need effort. One-word answers get treated like one-word effort.",
                    "Use the application form. Do not try to shortcut the process.",
                    "If you are applying, make it readable. Staff are not translators for laziness."
            ));

            replyGroups.put("Application Support", Arrays.asList(
                    "Application support detected. Say what part you are stuck on instead of waving vaguely at the form.",
                    "If the application is confusing, tell me which question. I am not inspecting your brain.",
                    "Application issue? Send the exact part you need help with.",
                    "You want help joining. Good. Now explain what is stopping you.",
                    "Tell me whether this is about applying, pending status, denial, or interview.",
                    "Application support needs context. Try providing some.",
                    "If you got denied, send the reason. If you are applying, fill it properly.",
                    "Say which stage you are at. Application, review, interview, or denial."
            ));

            replyGroups.put("Payment / Price", Arrays.asList(
                    "Price question detected. Say what you are trying to buy before asking like a fog machine.",
                    "Cost depends on the item. Name the product, genius.",
                    "Payment issue? Send the package, price, and what went wrong.",
                    "Refund, invoice, purchase, or subscription? Pick one and explain.",
                    "If you want a price, say what for. I cannot price invisible items.",
                    "Ask about the exact service or product. Vague money talk is useless.",
                    "Payment questions need details. Start with what you paid for.",
                    "Send what you bought, when you bought it, and what failed."
            ));

            replyGroups.put("Angry Payment Question", Arrays.asList(
                    "Angry payment issue detected. Send the invoice, package, and problem before kicking off.",
                    "You are mad about money. Understandable. Now give actual payment details.",
                    "If payment failed, say how. If you want a refund, say why.",
                    "Raging about payment without details helps nobody.",
                    "Send order info and the issue. That is how this gets solved.",
                    "Money complaint detected. Evidence required, not noise.",
                    "What did you buy, what happened, and what do you want done?",
                    "Payment issue? Provide facts before acting like the world ended."
            ));

            replyGroups.put("Discord / Link", Arrays.asList(
                    "Use the official link. Do not click random nonsense like an internet NPC.",
                    "Discord link request detected. Check official links before asking blindly.",
                    "If you need the invite, ask for the official one only.",
                    "Use verified links. Random invites are how chaos starts.",
                    "Server link? Ask staff or check the official section.",
                    "I am not handing you mystery links. Use the official invite.",
                    "Discord invite requests belong in the official links area.",
                    "Find the official link. It is usually not hidden in Narnia."
            ));

            replyGroups.put("Event / Schedule", Arrays.asList(
                    "Check announcements. That is where schedules live, believe it or not.",
                    "Event question detected. Look for the latest announcement first.",
                    "If the time is not announced, nobody knows. Shocking.",
                    "Ask about the exact event, not just 'when'.",
                    "Tournament, scrim, trial, or event? Be specific.",
                    "Schedules change. Check announcements before guessing.",
                    "Send the event name and I can point you in the right direction.",
                    "If you mean the next event, say that. Mind-reading is still unavailable."
            ));

            replyGroups.put("Thanks / Appreciation", Arrays.asList(
                    "You are welcome. Try not to break anything else.",
                    "No problem. That was almost polite.",
                    "You are welcome. Miracles happen.",
                    "Fine. Glad that helped.",
                    "No worries. Next time lead with details.",
                    "You are welcome. Keep the chaos contained.",
                    "Good. Sorted.",
                    "Finally, manners. You are welcome."
            ));

            replyGroups.put("Slang / Casual", Arrays.asList(
                    "I understood about half of that. Translate it into normal words.",
                    "Casual nonsense detected. Say the actual issue.",
                    "Bro, mate, whatever. What do you need?",
                    "Slang is fine. Missing context is not.",
                    "Type like you want help, not like you dropped your phone.",
                    "That sentence needs a mechanic.",
                    "Try again with fewer vibes and more information.",
                    "I can work with slang. I cannot work with emptiness."
            ));

            replyGroups.put("Common General", Arrays.asList(
                    "I picked up some common words, but you still need to explain yourself.",
                    "General intent detected. Very impressive. Now be specific.",
                    "You used support words but forgot the actual support request.",
                    "That message has keywords, not clarity.",
                    "I see what category this might be. Now provide details.",
                    "You are circling the point. Land the plane.",
                    "Common words detected. Useful explanation still pending.",
                    "Say exactly what you need."
            ));

            replyGroups.put("Mixed / Confused Message", Arrays.asList(
                    "That message is a pile of random words. Try again with an actual point.",
                    "You mixed greetings, slang, and nonsense together. Impressive waste of a sentence.",
                    "I detected words, not meaning. Rewrite that like you want an answer.",
                    "That made no clear sense. Stop throwing words at the wall and ask properly.",
                    "You said several things and somehow none of them became useful.",
                    "Random word soup detected. Try forming one complete thought.",
                    "That message has the structure of a dropped keyboard.",
                    "I can see keywords, but the actual request is missing.",
                    "You managed to say something and nothing at the same time.",
                    "Try again with fewer random words and more actual information."
            ));

            replyGroups.put("Aggressive Nonsense Greeting", Arrays.asList(
                    "You opened with a swear, slang, and a greeting. Pick a lane and ask properly.",
                    "Saying 'fuck mate hi' is not communication. It is verbal debris.",
                    "Greeting detected. Swearing detected. Useful question not detected.",
                    "That was a hostile hello with no purpose. Try again.",
                    "You sound annoyed, casual, and completely unclear all at once.",
                    "Hi, apparently. Now explain what you actually want.",
                    "That greeting came with unnecessary aggression and zero information.",
                    "If you need help, ask. If you just want to swear into the void, carry on.",
                    "You said hello like the sentence had a crash report.",
                    "Try again without making the bot guess whether you are greeting it or fighting it."
            ));

            replyGroups.put("Unknown / Fallback", Arrays.asList(
                    "Random words detected. I have no idea what that means.",
                    "That message means absolutely nothing. Try again with a sentence.",
                    "I cannot reply to keyboard soup. Use actual words.",
                    "No intent detected. Congratulations, you confused the bot.",
                    "That was not a message, that was a noise with letters.",
                    "I do not know what that means. Rewrite it like a human.",
                    "Unknown nonsense detected. Provide context.",
                    "That sentence fell down the stairs. Try again.",
                    "No clue what you are saying. Make it clearer.",
                    "That was impressively useless. Ask a real question."
            ));

            replyGroups.put("FollowUp Missing Details", Arrays.asList(
                    "That still tells me almost nothing. Give me the actual issue.",
                    "You replied, but somehow avoided the useful part. Explain clearly.",
                    "I asked for details, not another vague sentence.",
                    "Still vague. What happened, where did it happen, and what do you need?",
                    "You are making this harder than it needs to be. Give me the facts.",
                    "That is not enough information. Try again with actual context.",
                    "I need the problem, not the atmosphere around the problem.",
                    "You are circling the point. Land the plane."
            ));

            replyGroups.put("FollowUp Still Useless", Arrays.asList(
                    "That follow-up was just as useless as the first message.",
                    "You had a second chance and still produced nonsense.",
                    "Still no clear meaning. Rewrite it properly.",
                    "I cannot build an answer out of random words.",
                    "That clarified absolutely nothing.",
                    "Your follow-up made the problem worse, not clearer.",
                    "No useful intent detected again. Impressive consistency.",
                    "Try using a full sentence with an actual request."
            ));

            replyGroups.put("FollowUp Angry But Still Vague", Arrays.asList(
                    "You are still angry and still unclear. Terrible combination.",
                    "More swearing, still no useful details.",
                    "Rage is not context. Explain the actual issue.",
                    "You have successfully communicated that you are annoyed. Now communicate the problem.",
                    "Anger detected again. Solution not detected.",
                    "Stop throwing swear words at the chat and explain what happened.",
                    "You are typing frustration instead of information.",
                    "Still vague, just louder."
            ));

            replyGroups.put("FollowUp Bug Details", Arrays.asList(
                    "Good, now we are actually getting somewhere. Send the exact error and what caused it.",
                    "Bug context detected. Now give steps to reproduce it.",
                    "Better. Tell me what you clicked before it broke.",
                    "Now send the error message, screenshot, or exact behaviour.",
                    "That sounds like a bug. Give me reproduction steps.",
                    "Useful direction finally. What happened right before it failed?",
                    "Bug issue confirmed. Details, screenshots, and steps next.",
                    "Now explain whether it happens every time or only sometimes."
            ));

            replyGroups.put("FollowUp Support Details", Arrays.asList(
                    "Support context detected. Now explain what you need done.",
                    "Better. Give me the full issue clearly.",
                    "Now say what happened and what outcome you want.",
                    "Support request confirmed. Details next.",
                    "Good. Now stop being vague and explain the situation properly.",
                    "Tell me what you tried already.",
                    "Now provide the account, system, or feature involved.",
                    "That is closer. Give me the missing details."
            ));

            replyGroups.put("FollowUp Application Details", Arrays.asList(
                    "Application context detected. What part are you stuck on?",
                    "Good. Is this about applying, pending status, denial, or interview?",
                    "Now say which application you mean.",
                    "Application issue confirmed. Give the exact stage you are at.",
                    "Better. Are you trying to join, fix a form, or ask about review time?",
                    "Tell me what the application problem is specifically.",
                    "Now give the role, form, or question you are asking about.",
                    "Application support needs a specific question. Ask it."
            ));

            replyGroups.put("FollowUp Payment Details", Arrays.asList(
                    "Payment context detected. What did you buy and what went wrong?",
                    "Now send the package, price, and payment issue.",
                    "Good. Is this about price, refund, invoice, or failed payment?",
                    "Money issue confirmed. Give exact details.",
                    "Now provide what you paid for and what result you expected.",
                    "Payment problems need facts, not panic.",
                    "Tell me whether this is a refund, failed payment, or price question.",
                    "Better. Now give the actual transaction context."
            ));

            replyGroups.put("FollowUp Discord Details", Arrays.asList(
                    "Discord context detected. Are you asking for an invite, link issue, or server access?",
                    "Now say whether you need the invite or cannot access the server.",
                    "Good. What Discord problem are you having?",
                    "Discord issue confirmed. Give the exact problem.",
                    "If you need a link, ask for the official invite clearly.",
                    "If the invite failed, say what error showed.",
                    "Now explain whether the link is missing, expired, or blocked.",
                    "Better. Give the actual Discord issue."
            ));

            replyGroups.put("FollowUp Event Details", Arrays.asList(
                    "Event context detected. Which event are you asking about?",
                    "Now give the tournament, match, scrim, or trial name.",
                    "Good. What schedule are you asking for specifically?",
                    "Event question confirmed. Give the exact event.",
                    "If you mean the next event, say that clearly.",
                    "Now say whether this is about time, date, signup, or rules.",
                    "Better. Give the event name before asking when.",
                    "Schedule question detected. Specifics next."
            ));

            replyGroups.put("Repeated Greeting", Arrays.asList(
                    "You already said hello. Move on.",
                    "Yes, still here. Ask the question.",
                    "Second greeting detected. Still waiting for the useful part.",
                    "Hi again. Now say something with purpose.",
                    "We have completed the greeting stage. Continue.",
                    "Greeting twice does not unlock a secret menu.",
                    "Still here. Still waiting.",
                    "Hello again. Now get to the point."
            ));

            replyGroups.put("Repeated Unknown", Arrays.asList(
                    "That is the second nonsense message in a row.",
                    "You are building a streak of useless messages.",
                    "Still no clear intent. Rewrite it properly.",
                    "Again, I have no idea what that means.",
                    "You are not helping yourself here.",
                    "That is another pile of words with no request.",
                    "Try one normal sentence. Just one.",
                    "Unknown again. Painful."
            ));

            replyGroups.put("Repeated Swearing", Arrays.asList(
                    "Still swearing, still unclear.",
                    "You are repeating the rage but not the information.",
                    "More anger. Same lack of details.",
                    "Swearing twice still does not explain the issue.",
                    "You are not making progress here.",
                    "If you want help, explain the problem.",
                    "Your keyboard is suffering and I still know nothing.",
                    "Try facts instead of fury."
            ));
        }
    }

    static class DetectionReport {
        String rawMessage;
        String cleanedMessage;
        List<DetectedIntent> intents = new ArrayList<>();

        DetectionReport(String rawMessage, String cleanedMessage) {
            this.rawMessage = rawMessage;
            this.cleanedMessage = cleanedMessage;
        }

        boolean hasIntent(String name) {
            for (DetectedIntent intent : intents) {
                if (intent.name.equalsIgnoreCase(name)) {
                    return true;
                }
            }

            return false;
        }

        String toReadableText() {
            StringBuilder builder = new StringBuilder();

            String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

            builder.append("Detection Time: ").append(time).append("\n\n");
            builder.append("Raw Message:\n").append(rawMessage).append("\n\n");
            builder.append("Cleaned Message:\n").append(cleanedMessage).append("\n\n");
            builder.append("Detected Intents:\n");

            for (DetectedIntent intent : intents) {
                builder.append("- ")
                        .append(intent.name)
                        .append(" | Confidence: ")
                        .append(intent.confidence)
                        .append("%\n");

                if (!intent.matchedKeywords.isEmpty()) {
                    builder.append("  Matched: ")
                            .append(String.join(", ", intent.matchedKeywords))
                            .append("\n");
                }
            }

            return builder.toString();
        }
    }

    static class DetectedIntent {
        String name;
        int confidence;
        List<String> matchedKeywords;

        DetectedIntent(String name, int confidence, List<String> matchedKeywords) {
            this.name = name;
            this.confidence = confidence;
            this.matchedKeywords = matchedKeywords;
        }
    }
}
