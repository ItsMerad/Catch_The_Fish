package Catch_The_Fish;
import java.awt.*;
import javax.swing.*;

public class GameGUI extends JFrame {
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private JTextArea output = new JTextArea(4, 30);
    private JLabel scoreLabel = new JLabel();
    private JButton fishButton = new JButton("Balık Tut!");
    private JButton quitButton = new JButton("Balık Tutmayı Bırak");
    private JButton replayButton = new JButton("Yeniden Oyna");
    private JButton menuButton = new JButton("Ana Menüye Dön");
    private Game game;
    private int playerCount = 1;
    private String[] playerNames = new String[2];
    private int currentPlayer = 0;

    // Animasyon panelleri referansları
    private final SimpleHorizontalPanel horizontalPanel;
    private final SimpleVerticalPanel verticalPanel;
    private final MusicPlayer musicPlayer = new MusicPlayer();
    private final JButton soundButton = new JButton();
    private boolean soundOn = true;

    public GameGUI() {
        setTitle("Catch The Fish");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // MENÜ PANELİ (küçük)
        setSize(360, 250);
        setMinimumSize(new Dimension(360, 250));
        JPanel startPanel = new JPanel(new BorderLayout(10, 10));
        JLabel logoLabel = new JLabel();
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        java.net.URL logoUrl = getClass().getResource("/Catch_The_Fish/Logo.jpg");
        if (logoUrl != null) {
            ImageIcon icon = new ImageIcon(logoUrl);
            Image scaledImage = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(scaledImage));
        } else {
            logoLabel.setText("Logo bulunamadı!");
        }
        startPanel.add(logoLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        JLabel welcomeLabel = new JLabel("Catch The Fish", SwingConstants.CENTER);
        centerPanel.add(welcomeLabel);
        centerPanel.add(new JLabel("Kaç oyuncu ile oynamak istersiniz?", SwingConstants.CENTER));
        JButton onePlayerBtn = new JButton("1 Oyuncu");
        JButton twoPlayerBtn = new JButton("2 Oyuncu");
        centerPanel.add(onePlayerBtn);
        centerPanel.add(twoPlayerBtn);
        startPanel.add(centerPanel, BorderLayout.CENTER);

        mainPanel.add(startPanel, "start");

        // İSİM PANELİ
        JPanel namePanel = new JPanel(new GridLayout(4, 1, 10, 10));
        JLabel nameLabel = new JLabel("Oyuncu Adı Giriniz:", SwingConstants.CENTER);
        JTextField nameField1 = new JTextField("Balıkçı Hasan"); // Varsayılan değer
        JTextField nameField2 = new JTextField("Balıkçı Hasan2"); // İkinci oyuncu için varsayılan değer
        JButton nameOkBtn = new JButton("Başla");
        namePanel.add(nameLabel);
        namePanel.add(nameField1);
        namePanel.add(nameField2);
        namePanel.add(nameOkBtn);
        mainPanel.add(namePanel, "name");

        // OYUN PANELİ (büyük)
        JPanel gamePanel = new JPanel(new BorderLayout());

        // Bar ve horizontal animasyon panelini üst üste bindir
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(600, 500));

        // Bar Paneli (arka layer)
        JLabel barLabel = new JLabel() {
            private Image barImage;
            {
                java.net.URL barUrl = getClass().getResource("/Catch_The_Fish/Bar.png");
                if (barUrl != null) {
                    barImage = new ImageIcon(barUrl).getImage();
                }
            }
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (barImage != null) {
                    g.drawImage(barImage, 0, 0, 600, 500, this);
                }
            }
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(600, 500);
            }
        };
        barLabel.setBounds(0, 0, 600, 500);

        // Basit yatay animasyon paneli (barın üstünde)
        horizontalPanel = new SimpleHorizontalPanel(0);
        horizontalPanel.setOpaque(false);
        horizontalPanel.setBounds(0, 0, 600, 400);
        SimpleHorizontalPanel horizontalPanel1 = new SimpleHorizontalPanel(120);      // hemen başlar
        horizontalPanel1.setOpaque(false);
        horizontalPanel1.setBounds(0, 0, 600, 400);

        SimpleHorizontalPanel horizontalPanel2 = new SimpleHorizontalPanel(3000);    // 0.8 sn sonra başlar
        horizontalPanel2.setOpaque(false);
        horizontalPanel2.setBounds(0, 0, 600, 420);

        SimpleHorizontalPanel horizontalPanel3 = new SimpleHorizontalPanel(1600);   // 1.6 sn sonra başlar
        horizontalPanel3.setOpaque(false);
        horizontalPanel3.setBounds(0, 0, 600, 440);

        layeredPane.add(barLabel, Integer.valueOf(0));
        layeredPane.add(horizontalPanel1, Integer.valueOf(1));
        layeredPane.add(horizontalPanel2, Integer.valueOf(2));
        layeredPane.add(horizontalPanel3, Integer.valueOf(3));
        
        gamePanel.add(layeredPane, BorderLayout.NORTH);

        // Dikey animasyon paneli (örnek, bar ile ilgisi yok)
        verticalPanel = new SimpleVerticalPanel();
        verticalPanel.setBounds(540, 0, 60, 300); // Sağda ve üstte olacak şekilde
        layeredPane.add(verticalPanel, Integer.valueOf(4));

        // Metin kutusu ve skor
        JPanel outputPanel = new JPanel(new BorderLayout());
        output.setRows(4);
        output.setEditable(false);
        outputPanel.add(new JScrollPane(output), BorderLayout.CENTER);
        outputPanel.add(scoreLabel, BorderLayout.NORTH);
        gamePanel.add(outputPanel, BorderLayout.SOUTH);

        // Butonlar
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(fishButton);
        buttonPanel.add(quitButton);
        buttonPanel.add(replayButton);
        buttonPanel.add(menuButton);
        outputPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(gamePanel, "game");

        add(mainPanel);
        cardLayout.show(mainPanel, "start");

        // Başlangıçta sadece balık tut ve bırak butonları aktif olsun
        replayButton.setVisible(false);
        menuButton.setVisible(false);

        // Buton Aksiyonları
        onePlayerBtn.addActionListener(e -> {
            playerCount = 1;
            nameField2.setVisible(false);
            setSize(360, 250);
            setMinimumSize(new Dimension(360, 250));
            cardLayout.show(mainPanel, "name");
        });

        twoPlayerBtn.addActionListener(e -> {
            playerCount = 2;
            nameField2.setVisible(true);
            setSize(360, 250);
            setMinimumSize(new Dimension(360, 250));
            cardLayout.show(mainPanel, "name");
        });

        nameOkBtn.addActionListener(e -> {
            // Oyun ekranı açılırken pencereyi büyüt
            setSize(600, 650);
            setMinimumSize(new Dimension(600, 650));
            // Varsayılan isim: "Balıkçı Hasan"
            playerNames[0] = nameField1.getText().trim().isEmpty() ? "Balıkçı Hasan" : nameField1.getText().trim();
            playerNames[1] = playerCount == 2 ? (nameField2.getText().trim().isEmpty() ? "Balıkçı Hasan" : nameField2.getText().trim()) : null;
            game = new Game(playerCount);
            currentPlayer = 0;
            updateScoreLabel();
            output.setText("");
            fishButton.setEnabled(true);
            quitButton.setEnabled(true);
            replayButton.setVisible(false);
            menuButton.setVisible(false);
            cardLayout.show(mainPanel, "game");
        });

        fishButton.addActionListener(e -> {
            CatchableItem item = game.catchFish(currentPlayer);
            output.append(playerNames[currentPlayer] + " bir " + item.getName() + " tuttu! (" + item.getPoints() + " puan)\n");
            updateScoreLabel();
            if (playerCount == 2) {
                currentPlayer = 1 - currentPlayer;
                output.append("Sıra: " + playerNames[currentPlayer] + "\n");
            }
        });

        quitButton.addActionListener(e -> {
            output.append("\nOyun Bitti!\n");
            for (int i = 0; i < playerCount; i++) {
                output.append(playerNames[i] + " toplam puan: " + game.getPlayer(i).getScore() + "\n");
            }
            fishButton.setEnabled(false);
            quitButton.setEnabled(false);
            replayButton.setVisible(true);
            menuButton.setVisible(true);
        });

        replayButton.addActionListener(e -> {
            game = new Game(playerCount);
            currentPlayer = 0;
            updateScoreLabel();
            output.setText("");
            fishButton.setEnabled(true);
            quitButton.setEnabled(true);
            replayButton.setVisible(false);
            menuButton.setVisible(false);
            setSize(600, 650);
            setMinimumSize(new Dimension(600, 650));
        });

        menuButton.addActionListener(e -> {
            nameField1.setText("");
            nameField2.setText("");
            fishButton.setEnabled(true);
            quitButton.setEnabled(true);
            replayButton.setVisible(false);
            menuButton.setVisible(false);
            setSize(360, 250);
            setMinimumSize(new Dimension(360, 250));
            cardLayout.show(mainPanel, "start");
        });

        musicPlayer.play("/Catch_The_Fish/Music.wav", true);
        soundButton.setIcon(new ImageIcon(getClass().getResource("/Catch_The_Fish/soundon.png")));

        // Sound button'u layeredPane'e ekle (sol üstte, 50x50)
        soundButton.setBounds(10, 10, 50, 50);
        soundButton.setContentAreaFilled(false); // Arkaplanı kaldır
        soundButton.setBorderPainted(false);     // Kenarlık yok
        soundButton.setFocusPainted(false);      // Tıklama efekti yok
        soundButton.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/Catch_The_Fish/soundon.png")).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
        layeredPane.add(soundButton, Integer.valueOf(10));

        // Sound button action
        soundButton.addActionListener(e -> {
            if (soundOn) {
                musicPlayer.stop();
                soundButton.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/Catch_The_Fish/soundoff.png")).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
            } else {
                musicPlayer.play("/Catch_The_Fish/Music.wav", true);
                soundButton.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/Catch_The_Fish/soundon.png")).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
            }
            soundOn = !soundOn;
        });

        setVisible(true);
        setLocationRelativeTo(null); // Pencereyi ekranın orta üstüne yerleştir
    }

    private void updateScoreLabel() {
        if (playerCount == 1) {
            scoreLabel.setText(playerNames[0] + " Puan: " + game.getPlayer(0).getScore());
        } else {
            scoreLabel.setText(playerNames[0] + ": " + game.getPlayer(0).getScore() +
                    " | " + playerNames[1] + ": " + game.getPlayer(1).getScore() +
                    " | Sıra: " + playerNames[currentPlayer]);
        }
    }


    // --- Basit Yatay Panel ---
    class SimpleHorizontalPanel extends JPanel {
        private int x1, x2, x3;
        private final Image img1, img2, img3;

        public SimpleHorizontalPanel(int delay) {
            x1 = 0; x2 = -120; x3 = -240;
            img1 = loadImg("/Catch_The_Fish/HugeFish.png");
            img2 = loadImg("/Catch_The_Fish/OldShoe.png");
            img3 = loadImg("/Catch_The_Fish/SmallFish.png");

            // Timer'ı gecikmeli başlat
            new Timer(delay, e -> {
                Timer timer = new Timer(30, ev -> {
                    x1 += 4; x2 += 4; x3 += 4;
                    if (x1 > getWidth()) x1 = -40;
                    if (x2 > getWidth()) x2 = -40;
                    if (x3 > getWidth()) x3 = -40;
                    repaint();
                });
                timer.start();
                ((Timer)e.getSource()).stop();
            }).start();
        }

        private Image loadImg(String path) {
            java.net.URL url = getClass().getResource(path);
            return url != null ? new ImageIcon(url).getImage() : null;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (img1 != null) g.drawImage(img1, x1, getHeight() - 50, 40, 40, this);
            if (img2 != null) g.drawImage(img2, x2, getHeight() - 50, 40, 40, this);
            if (img3 != null) g.drawImage(img3, x3, getHeight() - 50, 40, 40, this);
        }
    }

    // --- Basit Dikey Panel ---
   class SimpleVerticalPanel extends JPanel {
    private int y = 0;
    private final Image img;

    public SimpleVerticalPanel() {
        setPreferredSize(new Dimension(60, 300));
        setOpaque(false);
        img = loadImg("/Catch_The_Fish/Leaf.png");
        if (img == null) System.out.println("ERROR: Leaf.png yüklenemedi!");
        Timer timer = new Timer(30, e -> {
            y += 2;
            if (y > getHeight()) y = -40;
            repaint();
        });
        timer.start();
    }

    private Image loadImg(String path) {
        java.net.URL url = getClass().getResource(path);
        if (url == null) System.out.println("ERROR: getResource null döndü: " + path);
        return url != null ? new ImageIcon(url).getImage() : null;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (img != null) {
            g.drawImage(img, 10, y, 40, 40, this);
        } else {
            System.out.println("ERROR: img (leaf) null, çizilemiyor!");
        }
    }


        @Override
        public Dimension getPreferredSize() {
            return new Dimension(60, 300); // veya ihtiyaca göre daha büyük
        }
    }
}