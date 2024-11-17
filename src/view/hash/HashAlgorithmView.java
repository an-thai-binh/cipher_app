package view.hash;

import model.hash.IHashAlgorithm;
import utils.CipherSupport;
import utils.FontUtils;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HashAlgorithmView extends JPanel {
    private final IHashAlgorithm algorithm;
    private CardLayout cardLayout;
    public HashAlgorithmView(IHashAlgorithm algorithm) {
        this.algorithm = algorithm;
        init();
    }

    /**
     * init hiển thị giao diện giải thuật hash
     */
    public void init() {
        cardLayout = new CardLayout();
        this.setLayout(cardLayout);
        // text panel
        HashTypeView textPanel = new HashTypeView(algorithm, true);
        // file panel
        HashTypeView filePanel = new HashTypeView(algorithm, false);
        this.add(textPanel, "text");
        this.add(filePanel, "file");
    }

    class HashTypeView extends JPanel implements ActionListener {
        private final IHashAlgorithm algorithm;
        private boolean isTextPanel;
        private JComboBox cbbMode;
        private JTextArea txtAreaInput, txtAreaOutput;
        public HashTypeView(IHashAlgorithm algorithm, boolean isTextPanel) {
            this.algorithm = algorithm;
            this.isTextPanel = isTextPanel;
            if(isTextPanel) {
                initText();
            } else {
                initFile();
            }
        }

        /**
         * initText hiển thị giao diện giải thuật hash (thao tác với văn bản)
         */
        private void initText() {
            renderSelf();
            renderTitleRow();
            renderTypeRow();
            if(algorithm.getName().equals("SHA")) {
                renderModeRow();
            }
            renderInputRow();
            renderActionRow();
            renderOutputRow();
        }

        /**
         * initFile hiển thị giao diện giải thuật hash (thao tác với file)
         */
        private void initFile() {
            renderSelf();
            renderTitleRow();
            renderTypeRow();
        }

        /**
         * renderSelf   cài đặt giao diện chính
         */
        private void renderSelf() {
            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            this.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            this.setBackground(Color.decode("#F4F6FF"));
        }

        /**
         * renderTitleRow   cài đặt giao diện dòng tiêu đề
         */
        private void renderTitleRow() {
            String title = algorithm.getName().toUpperCase();
            JLabel lblCipherTitle = new JLabel(title, JLabel.CENTER);
            lblCipherTitle.setFont(FontUtils.createRobotoFont("extraBold", 32f));
            lblCipherTitle.setForeground(Color.BLACK);
            lblCipherTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
            lblCipherTitle.setAlignmentX(Component.CENTER_ALIGNMENT);   // center trong boxlayout
            this.add(lblCipherTitle);
        }

        /**
         * renderTypeRow cài đặt giao diện dòng chọn loại thao tác (văn bản hoặc file)
         */
        private void renderTypeRow() {
            JPanel pnlType = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            pnlType.setPreferredSize(new Dimension(520, 50));
            pnlType.setBackground(null);
            JButton btnType = new JButton();
            btnType.setText(isTextPanel ? "Chuyển sang thao tác với File" : "Chuyển sang thao tác với văn bản");
            btnType.setBackground(Color.WHITE);
            btnType.setFont(FontUtils.createRobotoFont("medium", 16f));
            btnType.setForeground(Color.GRAY);
            btnType.setActionCommand("typeChange");
            btnType.addActionListener(this);
            pnlType.add(btnType);
            this.add(pnlType);
        }

        /**
         * renderModeRow    cài đặt giao diện dòng chọn chế độ (SHA)
         */
        private void renderModeRow() {
            JPanel pnlMode = new JPanel(new FlowLayout(FlowLayout.CENTER));
            pnlMode.setBackground(null);
            JLabel lblMode = new JLabel("Chọn chế độ:", JLabel.CENTER);
            lblMode.setFont(FontUtils.createRobotoFont("medium", 16f));
            pnlMode.add(lblMode);
            cbbMode = new JComboBox(CipherSupport.SHA_DIGESTS);
            cbbMode.setBackground(Color.WHITE);
            cbbMode.setPreferredSize(new Dimension(200, 30));
            cbbMode.setRenderer(new DefaultListCellRenderer() {
                @Override
                public void paint(Graphics g) {
                    setFont(FontUtils.createRobotoFont("medium", 20f));
                    setHorizontalAlignment(DefaultListCellRenderer.CENTER); // center item
                    super.paint(g);
                }
            });
            pnlMode.add(cbbMode);
            this.add(pnlMode);
        }

        /**
         * renderInputRow   cài đặt giao diện dòng input
         */
        private void renderInputRow() {
            txtAreaInput = new JTextArea();
            txtAreaInput.setLineWrap(true);
            txtAreaInput.setWrapStyleWord(true);
            txtAreaInput.setFont(FontUtils.createRobotoFont("regular", 16f));
            txtAreaInput.setForeground(Color.black);
            TitledBorder borderInput = BorderFactory.createTitledBorder("Nhập văn bản đầu vào");
            borderInput.setTitleFont(FontUtils.createRobotoFont("regular", 13f));
            txtAreaInput.setBorder(borderInput);
            // input scrollpane
            JScrollPane inputScrollPane = new JScrollPane(txtAreaInput);
            inputScrollPane.setPreferredSize(new Dimension(520, 185));
            inputScrollPane.setBackground(null);
            this.add(inputScrollPane);
        }

        /**
         * renderActionRow  cài đặt giao diện dòng action
         */
        private void renderActionRow() {
            JPanel pnlAction = new JPanel(new FlowLayout(FlowLayout.CENTER));
            pnlAction.setBackground(null);
            pnlAction.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
            JButton btnHash = new JButton("Hash");
            btnHash.setActionCommand("hash");
            btnHash.addActionListener(this);
            btnHash.setPreferredSize(new Dimension(200, 50));
            btnHash.setBackground(Color.decode("#F3C623"));
            btnHash.setFont(FontUtils.createRobotoFont("medium", 24f));
            pnlAction.add(btnHash);
            this.add(pnlAction);
        }

        /**
         * renderOutputRow  cài đặt giao diện dòng output
         */
        private void renderOutputRow() {
            txtAreaOutput = new JTextArea();
            txtAreaOutput.setLineWrap(true);
            txtAreaOutput.setWrapStyleWord(true);
            txtAreaOutput.setEditable(false);
            txtAreaOutput.setFont(FontUtils.createRobotoFont("regular", 16f));
            txtAreaOutput.setForeground(Color.black);
            TitledBorder borderOutput = BorderFactory.createTitledBorder("Kết quả");
            borderOutput.setTitleFont(FontUtils.createRobotoFont("regular", 13f));
            txtAreaOutput.setBorder(borderOutput);
            JScrollPane outputScrollPane = new JScrollPane(txtAreaOutput);
            outputScrollPane.setPreferredSize(new Dimension(520, 185));
            this.add(outputScrollPane);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String cmd = e.getActionCommand();
            switch (cmd) {
                case "typeChange": {
                    cardLayout.show(HashAlgorithmView.this, isTextPanel ? "file" : "text");
                    break;
                }
                case "hash": {
                    // check input
                    String text = txtAreaInput.getText();
                    if(text.isEmpty()) {
                        showErrorDialog("Vui lòng nhập văn bản đầu vào");
                        break;
                    }
                    // set instance
                    try {
                        String name = algorithm.getName();
                        if(name.equals("SHA")) {    // nếu là SHA thì lấy instance từ cbbMode
                            algorithm.setInstance(cbbMode.getSelectedItem().toString());
                        } else {
                            algorithm.setInstance(algorithm.getName());
                        }
                    } catch (Exception ex) {
                        showErrorDialog(ex.getMessage());
                        break;
                    }
                    txtAreaOutput.setText(algorithm.hash(text));
                    break;
                }
                case "hashFile": {
                    break;
                }
            }
        }

        /**
         * showErrorDialog	hiển thị cửa sổ thông báo lỗi
         * @param message	thông báo
         */
        public void showErrorDialog(String message) {
            JOptionPane.showMessageDialog(this.getParent(), message, "Lỗi thực thi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
