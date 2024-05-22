import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class BookReader {
     JEditorPane textPane;    // --> text holder
     JButton nextButton;      // next button
     JButton prevButton;      // previous button
     JLabel titleLabel;       // Story Title at the top
     JLabel pageNumberLabel;  // for page number
     JLabel imageLabel;       // Image Holder
     String[] textParts;      // helper variable to store entire story into parts
     int currentPage;         // current page number
     int totalPages;          // total number of pages
     String[] imagePaths;     // all the images in an order for all the pages

    public BookReader(String title, String text, int wordsPerPage, String backgroundImagePath, String[] imagePaths) {
        this.imagePaths = imagePaths;
        textParts = split(text, wordsPerPage);
        currentPage = 0;
        totalPages = textParts.length;

        JFrame frame = new JFrame("Book Reader");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 660);

        BackgroundPanel backgroundPanel = new BackgroundPanel(backgroundImagePath);

        textPane = new JEditorPane();
        textPane.setContentType("text/html");
        textPane.setEditable(false);
        textPane.setOpaque(false);
        textPane.setMargin(new Insets(10, 45, 10, 30));
        JScrollPane scrollPane = new JScrollPane(textPane);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.PLAIN, 45));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 10, 0, 10));
        titleLabel.setOpaque(false);

        nextButton = new JButton("Next");
        prevButton = new JButton("Previous");
        prevButton.setEnabled(false);

        nextButton.addActionListener(e -> {
            if (currentPage < totalPages - 1) {
                currentPage++;
                updateText();
            }
            if (currentPage == totalPages - 1) {
                nextButton.setEnabled(false);
            }
            if (!prevButton.isEnabled()) {
                prevButton.setEnabled(true);
            }
        });

        prevButton.addActionListener(e -> {
            if (currentPage > 0) {
                currentPage--;
                updateText();
            }
            if (currentPage == 0) {
                prevButton.setEnabled(false);
            }
            if (!nextButton.isEnabled()) {
                nextButton.setEnabled(true);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(prevButton);
        buttonPanel.add(nextButton);

        pageNumberLabel = new JLabel("Page " + (currentPage + 1) + " of " + totalPages, SwingConstants.CENTER);
        pageNumberLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 30, 0)); // Add some padding around the page number
        pageNumberLabel.setOpaque(false);

        JPanel pagePanel = new JPanel(new BorderLayout());
        pagePanel.setFont(new Font("Fantasy", Font.PLAIN, 25));
        pagePanel.setOpaque(false);
        pagePanel.add(buttonPanel, BorderLayout.CENTER);
        pagePanel.add(pageNumberLabel, BorderLayout.SOUTH);

        imageLabel = new JLabel();
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        imageLabel.setPreferredSize(new Dimension(500, 250));
        imageLabel.setOpaque(false);

        JPanel imagePanel = new JPanel(new GridBagLayout());
        imagePanel.setOpaque(false);
        imagePanel.setBorder(BorderFactory.createEmptyBorder(35, 0, 25,0));
        imagePanel.add(imageLabel);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        contentPanel.add(imagePanel, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        backgroundPanel.add(titleLabel, BorderLayout.NORTH);
        backgroundPanel.add(contentPanel, BorderLayout.CENTER);
        backgroundPanel.add(pagePanel, BorderLayout.SOUTH);
        frame.getContentPane().add(backgroundPanel);
        imageLabel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (imageLabel.getWidth() > 0 && imageLabel.getHeight() > 0) {
                    updateImage();
                }
            }
        });
        imageLabel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (imageLabel.getWidth() > 0 && imageLabel.getHeight() > 0) {
                    updateImage();
                }
            }
        });
        imageLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    ImageIcon icon = (ImageIcon) imageLabel.getIcon();
                    if (icon != null) {
                        openImageInNewFrame(new ImageIcon(imagePaths[currentPage]));
                    }
                }
            }
        });
        updateText();
        frame.setVisible(true);
    }

    private void updateText() {
        String formattedText = getString();
        textPane.setText(formattedText);
        pageNumberLabel.setText("Page " + (currentPage + 1) + " of " + totalPages);
        titleLabel.setVisible(currentPage == 0);
        if (imagePaths != null && currentPage < imagePaths.length && imagePaths[currentPage] != null) {
            if (imageLabel.getWidth() > 0 && imageLabel.getHeight() > 0) {
                updateImage();
            } else {
                imageLabel.setIcon(null);
            }
        } else {
            imageLabel.setIcon(null);
        }
    }

    private String getString() {
        String currentText = textParts[currentPage];
        String formattedText;
        if (currentPage == 0) {
            formattedText = "<html>" +
                    "<span style='font-size:25px; font-family: Papyrus, fantasy;'>" +
                    currentText.charAt(0) + "</span>" +
                    currentText.substring(1) +
                    "</html>";
        } else {
            formattedText = "<html>" + currentText + "</html>";
        }
        return formattedText;
    }

    private void updateImage() {
        if (imagePaths != null && currentPage < imagePaths.length && imagePaths[currentPage] != null) {
            ImageIcon icon = new ImageIcon(imagePaths[currentPage]);
            Image originalImage = icon.getImage();
            if (imageLabel.getWidth() > 0 && imageLabel.getHeight() > 0) {
                Image scaledImage = originalImage.getScaledInstance(
                        imageLabel.getWidth(),
                        imageLabel.getHeight(),
                        Image.SCALE_SMOOTH
                );
                imageLabel.setIcon(new ImageIcon(scaledImage));
            }
        } else {
            imageLabel.setIcon(null);
        }
    }

    private String[] split(String text, int wordsPerPage) {
        String[] words = text.split("\\s+");
        int numChunks = (int) Math.ceil((double) words.length / wordsPerPage);
        String[] chunks = new String[numChunks];
        StringBuilder chunk = new StringBuilder();
        int chunkIndex = 0;
        for (int i = 0; i < words.length; i++) {
            chunk.append(words[i]).append(" ");
            if ((i + 1) % wordsPerPage == 0 || i == words.length - 1) {
                chunks[chunkIndex++] = chunk.toString().trim();
                chunk.setLength(0);
            }
        }
        return chunks;
    }

    private void openImageInNewFrame(ImageIcon icon) {
        JFrame imageFrame = new JFrame("Image");
        JLabel imageLabel = new JLabel(icon);
        imageFrame.getContentPane().add(imageLabel);
        imageFrame.setSize(icon.getIconWidth(), icon.getIconHeight());
        imageFrame.setVisible(true);
    }

}
