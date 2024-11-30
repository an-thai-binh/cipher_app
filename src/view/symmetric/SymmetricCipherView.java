package view.symmetric;

import model.symmetric.SymmetricCipher;
import utils.FontUtils;
import utils.IconUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Base64;
import java.util.List;

public class SymmetricCipherView extends JPanel {
    private final SymmetricCipher cipher;
    private CardLayout cardLayout;

    public SymmetricCipherView(SymmetricCipher cipher) {
        this.cipher = cipher;
        init();
    }

    /**
     * init cài đặt giao diện cho cardlayout
     */
    private void init() {
        cardLayout = new CardLayout();
        this.setLayout(cardLayout);
        // text panel
        SymmetricTypeView textPanel = new SymmetricTypeView(cipher,true);
        // file panel
        SymmetricTypeView filePanel = new SymmetricTypeView(cipher, false);
        this.add(textPanel, "text");
        this.add(filePanel, "file");
    }

    class SymmetricTypeView extends JPanel implements ActionListener {
        private final SymmetricCipher cipher;
        private boolean isTextPanel;
        private DefaultComboBoxModel<String> defaultPaddingModel, noPaddingModel;
        private JComboBox cbbKeySize, cbbMode, cbbPadding;
        private JTextField txtFieldKeySize, txtFieldIv, txtFieldInput, txtFieldOutput;
        private JTextArea txtAreaKey, txtAreaInput, txtAreaOutput;
        private JButton btnGenIv, btnCopyKey, btnOpenFileLocation;
        private JLabel lblStatus, lblResultPath;

        public SymmetricTypeView(SymmetricCipher cipher, boolean isTextPanel) {
            this.cipher = cipher;
            this.isTextPanel = isTextPanel;
            this.defaultPaddingModel = new DefaultComboBoxModel<>(cipher.getSupportedPadding().toArray(new String[0]));
            this.noPaddingModel = new DefaultComboBoxModel<>(new String[]{"NoPadding"});
            this.renderSelf();
            if(isTextPanel)
                initText();
            else
                initFile();
        }

        /**
         * initText hiển thị giao diện mã hóa đối xứng (thao tác với văn bản)
         */
        private void initText() {
            renderTitleRow();
            renderTypeRow();
            renderInputRow();
            renderMarginRow(0, 0, 0, 0);
            renderKeyRow();
            renderMarginRow(0, 0, 0, 0);
            renderActionRow();
            renderOutputRow();
        }

        /**
         * initFile hiển thị giao diện mã hóa đối xứng (thao tác với file)
         */
        private void initFile() {
            renderTitleRow();
            renderTypeRow();
            renderChooseInputFileRow();
            renderInputFileRow();
            renderKeyRow();
            renderChooseOutputFileRow();
            renderActionRow();
            renderOutputFileRow();
        }

        /**
         * renderMarginRow  chèn khoảng cách
         * @param top   top margin
         * @param left  left margin
         * @param bottom    bottom margin
         * @param right right margin
         */
        private void renderMarginRow(int top, int left, int bottom, int right) {
            JPanel pnlMargin = new JPanel();
            pnlMargin.setPreferredSize(new Dimension(0, 0));
            pnlMargin.setBackground(null);
            pnlMargin.setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));
            this.add(pnlMargin);
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
            String title = cipher.getName().toUpperCase();
            JLabel lblCipherTitle = new JLabel(title, JLabel.CENTER);
            lblCipherTitle.setFont(FontUtils.createRobotoFont("extraBold", 32f));
            lblCipherTitle.setForeground(Color.BLACK);
            lblCipherTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
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
         * renderInputRow   cài đặt giao diện dòng input
         */
        private void renderInputRow() {
            txtAreaInput = new JTextArea();
            txtAreaInput.setLineWrap(true);
            txtAreaInput.setWrapStyleWord(true);
            txtAreaInput.setFont(FontUtils.createRobotoFont("regular", 16f));
            txtAreaInput.setForeground(Color.black);
            TitledBorder borderInput = BorderFactory.createTitledBorder("Nhập văn bản cần mã hóa/giải mã");
            borderInput.setTitleFont(FontUtils.createRobotoFont("regular", 13f));
            txtAreaInput.setBorder(borderInput);
            // input scrollpane
            JScrollPane inputScrollPane = new JScrollPane(txtAreaInput);
            inputScrollPane.setPreferredSize(new Dimension(0, 185));
            inputScrollPane.setBackground(null);
            this.add(inputScrollPane);
        }

        /**
         * renderChooseInputFileRow   cài đặt giao diện dòng chọn input file
         */
        private void renderChooseInputFileRow() {
            JPanel pnlInputFile = new JPanel(new GridLayout(1, 1));
            pnlInputFile.setBackground(null);
            pnlInputFile.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
            // button input file
            JButton btnInputFile = new JButton("CHỌN FILE CẦN MÃ HÓA/GIẢI MÃ");
            btnInputFile.setVerticalTextPosition(SwingConstants.TOP);
            btnInputFile.setHorizontalTextPosition(SwingConstants.CENTER);
            btnInputFile.setBackground(Color.WHITE);
            btnInputFile.setFont(FontUtils.createRobotoFont("medium", 20f));
            btnInputFile.setIcon(IconUtils.UPLOAD_ICON);
            btnInputFile.setActionCommand("chooseInput");
            btnInputFile.addActionListener(this);
            btnInputFile.setDropTarget(new DropTarget() {
                @Override
                public synchronized void drop(DropTargetDropEvent dtde) {
                    try {
                        dtde.acceptDrop(DnDConstants.ACTION_COPY);
                        List<File> droppedFiles = (List<File>) dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                        if(droppedFiles.size() == 1) {
                            File file = droppedFiles.get(0);
                            if(!file.isDirectory()) {
                                txtFieldInput.setText(file.getAbsolutePath());
                            } else {
                                showErrorDialog("Không thể thao tác với thư mục");
                            }
                        } else {
                            showErrorDialog("Chương trình chỉ nhận 1 file cùng lúc");
                        }
                    } catch (Exception e){
                        showErrorDialog("Lỗi trong quá trình tải file lên");
                    }
                }
            });
            pnlInputFile.add(btnInputFile, BorderLayout.CENTER);
            this.add(pnlInputFile);
        }

        /**
         * renderInputFileRow   cài đặt giao diện dòng thông tin input file
         */
        private void renderInputFileRow() {
            JPanel pnlChooseInput = new JPanel(new GridLayout(1, 1));
            pnlChooseInput.setBackground(null);
            pnlChooseInput.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
            JPanel pnlInputPath = new JPanel();
            pnlInputPath.setLayout(new BoxLayout(pnlInputPath, BoxLayout.X_AXIS));
            pnlInputPath.setBackground(null);
            JLabel lblInputPath = new JLabel("Đường dẫn file nguồn:");
            lblInputPath.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
            lblInputPath.setFont(FontUtils.createRobotoFont("medium", 16f));
            txtFieldInput = new JTextField();
            txtFieldInput.setFont(FontUtils.createRobotoFont("regular", 16f));
            pnlInputPath.add(lblInputPath);
            pnlInputPath.add(txtFieldInput);
            pnlChooseInput.add(pnlInputPath);
            this.add(pnlChooseInput);
        }

        /**
         * renderKeyRow cài đặt giao diện dòng key
         */
        private void renderKeyRow() {
            JPanel pnlKeyRow = new JPanel(new GridLayout(1, 2));
            pnlKeyRow.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            // KHU VỰC BÊN TRÁI
            JPanel pnlSetting = new JPanel(new GridLayout(4, 1));
            // key size
            JPanel pnlKeySize = new JPanel(new BorderLayout());
            JLabel lblKeySize = new JLabel("Kích thước key", JLabel.CENTER);
            lblKeySize.setPreferredSize(new Dimension(150, 0));
            lblKeySize.setFont(FontUtils.createRobotoFont("regular", 20f));
            if(cipher.isFixedKeySize()) {   //  kiểm tra nếu cipher có kích thuớc khóa cố định
                cbbKeySize = new JComboBox(cipher.getSupportedKeySize().toArray(new Integer[0]));
                cbbKeySize.setBackground(Color.WHITE);
                cbbKeySize.setRenderer(new DefaultListCellRenderer() {
                    @Override
                    public void paint(Graphics g) {
                        setFont(FontUtils.createRobotoFont("medium", 20f));
                        setHorizontalAlignment(DefaultListCellRenderer.CENTER); // center item
                        super.paint(g);
                    }
                });
                pnlKeySize.add(lblKeySize, BorderLayout.WEST);
                pnlKeySize.add(cbbKeySize, BorderLayout.CENTER);
            } else {
                txtFieldKeySize = new JTextField();
                txtFieldKeySize.setFont(FontUtils.createRobotoFont("medium", 20f));
                txtFieldKeySize.setHorizontalAlignment(JTextField.CENTER);
                txtFieldKeySize.setToolTipText("Key có kích thước nằm trong khoảng từ " + cipher.getSupportedKeySize().get(0) + " đến " + cipher.getSupportedKeySize().get(1));
                pnlKeySize.add(lblKeySize, BorderLayout.WEST);
                pnlKeySize.add(txtFieldKeySize, BorderLayout.CENTER);
            }
            pnlSetting.add(pnlKeySize);
            // mode
            JPanel pnlMode = new JPanel(new BorderLayout());
            JLabel lblMode = new JLabel("Mode", JLabel.CENTER);
            lblMode.setPreferredSize(new Dimension(150, 0));
            lblMode.setFont(FontUtils.createRobotoFont("regular", 20f));
            cbbMode = new JComboBox(cipher.getSupportedMode().toArray(new String[0]));
            cbbMode.setBackground(Color.WHITE);
            cbbMode.setRenderer(new DefaultListCellRenderer() {
                @Override
                public void paint(Graphics g) {
                    setFont(FontUtils.createRobotoFont("medium", 20f));
                    setHorizontalAlignment(DefaultListCellRenderer.CENTER); // center item
                    super.paint(g);
                }
            });
            if(cipher.getSupportedMode().get(0).equals("")) cbbMode.setEnabled(false);
            cbbMode.setActionCommand("modeChange");
            cbbMode.addActionListener(this);
            pnlMode.add(lblMode, BorderLayout.WEST);
            pnlMode.add(cbbMode, BorderLayout.CENTER);
            pnlSetting.add(pnlMode);
            // padding
            JPanel pnlPadding = new JPanel(new BorderLayout());
            JLabel lblPadding = new JLabel("Padding", JLabel.CENTER);
            lblPadding.setPreferredSize(new Dimension(150, 0));
            lblPadding.setFont(FontUtils.createRobotoFont("regular", 20f));
            cbbPadding = new JComboBox(defaultPaddingModel);
            cbbPadding.setBackground(Color.WHITE);
            cbbPadding.setRenderer(new DefaultListCellRenderer() {
                @Override
                public void paint(Graphics g) {
                    setFont(FontUtils.createRobotoFont("medium", 20f));
                    setHorizontalAlignment(DefaultListCellRenderer.CENTER); // center item
                    super.paint(g);
                }
            });
            if(cipher.getSupportedPadding().get(0).equals("")) cbbPadding.setEnabled(false);
            pnlPadding.add(lblPadding, BorderLayout.WEST);
            pnlPadding.add(cbbPadding, BorderLayout.CENTER);
            pnlSetting.add(pnlPadding);
            // iv
            JPanel pnlIv = new JPanel(new BorderLayout());
            JLabel lblIv = new JLabel("IV", JLabel.CENTER);
            lblIv.setPreferredSize(new Dimension(150, 0));
            lblIv.setFont(FontUtils.createRobotoFont("regular", 20f));
            txtFieldIv = new JTextField();
            txtFieldIv.setFont(FontUtils.createRobotoFont("regular", 16f));
            btnGenIv = new JButton(IconUtils.GENERATE_ICON);
            btnGenIv.setBackground(Color.decode("#EEEEEE"));
            btnGenIv.setToolTipText("Tạo IV ngẫu nhiên");
            pnlIv.add(lblIv, BorderLayout.WEST);
            pnlIv.add(txtFieldIv, BorderLayout.CENTER);
            pnlIv.add(btnGenIv, BorderLayout.EAST);
            btnGenIv.setActionCommand("genIv");
            btnGenIv.addActionListener(this);
            if(cbbMode.getSelectedItem().toString().equals("ECB") || cipher.getSupportedIvOrNonceSize() == 0) {
                txtFieldIv.setEnabled(false);
                btnGenIv.setEnabled(false);
            }
            pnlSetting.add(pnlIv);
            pnlKeyRow.add(pnlSetting);

            // KHU VỰC BÊN PHẢI
            JPanel pnlKey = new JPanel();
            pnlKey.setLayout(new BoxLayout(pnlKey, BoxLayout.X_AXIS));
            pnlKey.setBackground(null);
            // key icon
            JPanel pnlKeyIcon = new JPanel(new GridLayout(1, 1));
            pnlKeyIcon.setPreferredSize(new Dimension(74, 134));
            pnlKeyIcon.setBackground(Color.decode("#EB8317"));
            pnlKeyIcon.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            JLabel lblKeyIcon = new JLabel(IconUtils.KEY_ICON, JLabel.CENTER);
            pnlKeyIcon.add(lblKeyIcon);
            pnlKey.add(pnlKeyIcon);
            // key text area
            txtAreaKey = new JTextArea();
            txtAreaKey.setFont(FontUtils.createRobotoFont("regular", 16f));
            txtAreaKey.setLineWrap(true);
            txtAreaKey.setWrapStyleWord(true);
            JScrollPane keyScrollPane = new JScrollPane(txtAreaKey);
            keyScrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            keyScrollPane.setPreferredSize(new Dimension(402, 134));
            pnlKey.add(keyScrollPane);
            // key tool
            JPanel pnlKeyTool = new JPanel(new GridLayout(3, 1));
            pnlKeyTool.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            JButton btnGenKey = new JButton("Tạo key");
            btnGenKey.setActionCommand("genKey");
            btnGenKey.addActionListener(this);
            btnGenKey.setBackground(Color.decode("#EEEEEE"));
            btnGenKey.setFont(FontUtils.createRobotoFont("regular", 20f));
            btnGenKey.setPreferredSize(new Dimension(128, 44));
            btnCopyKey = new JButton("Sao chép");
            btnCopyKey.setActionCommand("copy");
            btnCopyKey.addActionListener(this);
            btnCopyKey.setBackground(Color.decode("#EEEEEE"));
            btnCopyKey.setFont(FontUtils.createRobotoFont("regular", 20f));
            JPanel pnlSaveLoad = new JPanel(new GridLayout(1, 2));
            JButton btnSaveKey = new JButton(IconUtils.SAVE_ICON);
            btnSaveKey.setActionCommand("saveKey");
            btnSaveKey.addActionListener(this);
            btnSaveKey.setPreferredSize(new Dimension(64, 44));
            btnSaveKey.setBackground(Color.decode("#EEEEEE"));
            btnSaveKey.setToolTipText("Lưu key về máy dưới dạng file .dat");
            JButton btnLoadKey = new JButton(IconUtils.LOAD_ICON);
            btnLoadKey.setActionCommand("loadKey");
            btnLoadKey.addActionListener(this);
            btnLoadKey.setPreferredSize(new Dimension(64, 44));
            btnLoadKey.setBackground(Color.decode("#EEEEEE"));
            btnLoadKey.setToolTipText("Tải key lên từ file .dat");
            // thêm vào panel
            pnlSaveLoad.add(btnSaveKey);
            pnlSaveLoad.add(btnLoadKey);
            pnlKeyTool.add(btnCopyKey);
            pnlKeyTool.add(btnGenKey);
            pnlKeyTool.add(pnlSaveLoad);
            pnlKey.add(pnlKeyTool);
            pnlKeyRow.add(pnlKey);

            this.add(pnlKeyRow);
        }

        /**
         * renderActionRow  cài đặt giao diện dòng action
         */
        private void renderActionRow() {
            JPanel pnlAction = new JPanel(new FlowLayout(FlowLayout.CENTER));
            pnlAction.setBackground(null);
            JButton btnEncrypt = new JButton("Mã hóa");
            btnEncrypt.setPreferredSize(new Dimension(200, 50));
            btnEncrypt.setActionCommand(isTextPanel ? "encrypt" : "encryptFile");
            btnEncrypt.addActionListener(this);
            btnEncrypt.setBackground(Color.decode("#F3C623"));
            btnEncrypt.setFont(FontUtils.createRobotoFont("medium", 24f));
            JButton btnDecrypt = new JButton("Giải mã");
            btnDecrypt.setPreferredSize(new Dimension(200, 50));
            btnDecrypt.setActionCommand(isTextPanel ? "decrypt" : "decryptFile");
            btnDecrypt.addActionListener(this);
            btnDecrypt.setBackground(Color.decode("#F3C623"));
            btnDecrypt.setFont(FontUtils.createRobotoFont("medium", 24f));
            pnlAction.add(btnEncrypt);
            pnlAction.add(btnDecrypt);
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

        /**
         * renderChooseOutputFileRow  cài đặt giao diện dòng chọn output file
         */
        private void renderChooseOutputFileRow() {
            JPanel pnlChooseFile = new JPanel(new GridLayout(1, 1));
            pnlChooseFile.setBackground(null);
            pnlChooseFile.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
            JPanel pnlOutputPath = new JPanel();
            pnlOutputPath.setBackground(null);
            pnlOutputPath.setLayout(new BoxLayout(pnlOutputPath, BoxLayout.X_AXIS));
            JLabel lblChoosePath = new JLabel("Đường dẫn file đích:");
            lblChoosePath.setFont(FontUtils.createRobotoFont("medium", 16f));
            txtFieldOutput = new JTextField();
            txtFieldOutput.setFont(FontUtils.createRobotoFont("regular", 16f));
            JButton btnChoosePath = new JButton("Chọn đường dẫn");
            btnChoosePath.setToolTipText("Chọn đường dẫn file đích chứa kết quả");
            btnChoosePath.setFont(FontUtils.createRobotoFont("medium", 16f));
            btnChoosePath.setBackground(Color.decode("#EEEEEE"));
            btnChoosePath.setActionCommand("chooseOutput");
            btnChoosePath.addActionListener(this);
            pnlOutputPath.add(lblChoosePath);
            pnlOutputPath.add(txtFieldOutput);
            pnlOutputPath.add(btnChoosePath);
            pnlChooseFile.add(pnlOutputPath);
            this.add(pnlChooseFile);
        }

        /**
         * renderOutputFileRow  cài đặt giao diện dòng thông tin output file
         */
        private void renderOutputFileRow() {
            JPanel pnlOutputFile = new JPanel(new GridLayout(3, 1));
            pnlOutputFile.setBackground(null);
            pnlOutputFile.setPreferredSize(new Dimension(0, 185));
            // status
            lblStatus = new JLabel("Kết quả: ");
            lblStatus.setFont(FontUtils.createRobotoFont("medium", 16f));
            // file path
            lblResultPath = new JLabel("", JLabel.CENTER);
            lblResultPath.setFont(FontUtils.createRobotoFont("regular", 16f));
            // tool
            JPanel pnlTool = new JPanel(new FlowLayout(FlowLayout.CENTER));
            pnlTool.setBackground(null);
            btnOpenFileLocation = new JButton("Mở thư mục chứa file");
            btnOpenFileLocation.setFont(FontUtils.createRobotoFont("medium", 16f));
            btnOpenFileLocation.setBackground(Color.decode("#EEEEEE"));
            btnOpenFileLocation.setVisible(false);
            btnOpenFileLocation.setActionCommand("openFileLocation");
            btnOpenFileLocation.addActionListener(this);
            pnlTool.add(btnOpenFileLocation);
            // add
            pnlOutputFile.add(lblStatus);
            pnlOutputFile.add(lblResultPath);
            pnlOutputFile.add(pnlTool);
            this.add(pnlOutputFile);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String cmd = e.getActionCommand();
            switch (cmd) {
                case "typeChange": {
                    cardLayout.show(SymmetricCipherView.this, isTextPanel ? "file" : "text");
                }
                case "modeChange": {
                    String mode = cbbMode.getSelectedItem().toString();
                    // xử lý khi chọn các mode không cần padding
                    if(mode.equals("CTR") || mode.equals("CFB") || mode.equals("OFB")){
                        cbbPadding.setModel(noPaddingModel);
                    } else {
                        cbbPadding.setModel(defaultPaddingModel);
                    }
                    // xử lý khi chọn mode không cần IV hoặc Nonce
                    if(mode.equals("ECB") || cipher.getSupportedIvOrNonceSize() == 0) {
                        txtFieldIv.setText("");
                        txtFieldIv.setEnabled(false);
                        btnGenIv.setEnabled(false);
                    } else {
                        txtFieldIv.setEnabled(true);
                        btnGenIv.setEnabled(true);
                    }
                    break;
                }
                case "genIv": {
                    txtFieldIv.setText(cipher.generateRandomIv(cipher.getSupportedIvOrNonceSize()));
                    break;
                }
                case "copy": {  // sự kiện sao chép key
                    String text = txtAreaKey.getText();
                    StringSelection selection = new StringSelection(txtAreaKey.getText());
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    clipboard.setContents(selection, null);
                    // kiểm tra nội dung trong clipboard
                    Transferable transferData = clipboard.getContents(null);
                    if(transferData != null && transferData.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                        try {
                            String copiedText = (String) transferData.getTransferData(DataFlavor.stringFlavor);
                            if(copiedText.equals(text)) {
                                btnCopyKey.setBackground(Color.decode("#EDEBB9"));
                                Timer timer = new Timer(1000, new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        btnCopyKey.setBackground(Color.decode("#D9D9D9"));
                                    }
                                });
                                timer.setRepeats(false);
                                timer.start();
                            } else {
                                btnCopyKey.setBackground(Color.decode("#D9D9D9"));
                            }
                        } catch (Exception ex) {
                            btnCopyKey.setBackground(Color.decode("#D9D9D9"));
                        }
                    }
                    break;
                }
                case "genKey": {
                    String keySize = (cipher.isFixedKeySize() ? cbbKeySize.getSelectedItem().toString() : txtFieldKeySize.getText());
                    if(!cipher.isFixedKeySize()) {   // kiểm tra thêm nếu giải thuật có kích thước khóa không cố định
                        if(keySize.isBlank()) {
                            showErrorDialog("Vui lòng nhập kích thước key");
                            break;
                        }
                        int minKeySize = cipher.getSupportedKeySize().get(0);
                        int maxKeySize = cipher.getSupportedKeySize().get(1);
                        try {
                            int key = Integer.parseInt(keySize);
                            if(key < minKeySize || key > maxKeySize) {
                                showErrorDialog("Vui lòng nhập kích thước key trong khoảng từ " + minKeySize + " đến " + maxKeySize);
                                break;
                            }
                        } catch (Exception ex) {
                            showErrorDialog("Vui lòng nhập kích thước key trong khoảng từ " + minKeySize + " đến " + maxKeySize);
                            break;
                        }
                    }
                    // tạo secret key
                    try {
                        SecretKey key = cipher.genKey((Integer.parseInt(keySize)));
                        txtAreaKey.setText(Base64.getEncoder().encodeToString(key.getEncoded()));
                    } catch (Exception ex) {
                        showErrorDialog(ex.getMessage());
                    }
                    break;
                }
                case "encrypt": {
                    String text = txtAreaInput.getText();
                    if(text.isEmpty()) {
                        showErrorDialog("Vui lòng nhập nội dung cần mã hóa");
                        break;
                    }
                    // set key
                    try {
                        byte[] keyBytes = Base64.getDecoder().decode(txtAreaKey.getText());
                        SecretKey key = new SecretKeySpec(keyBytes, cipher.getName());
                        cipher.loadKey(key);
                    } catch (Exception ex){
                        showErrorDialog("Key không hợp lệ");
                        break;
                    }
                    // set mode, padding và encrypt
                    try {
                        String mode = cbbMode.getSelectedItem().toString();
                        String padding = cbbPadding.getSelectedItem().toString();
                        cipher.setCipher(mode, padding);
                        if(txtFieldIv.isEnabled()) {    // nếu txtFieldIv được bật thì yêu cầu truyền IV
                            if(!txtFieldIv.getText().isBlank()) {
                                cipher.setIvParameterSpec(txtFieldIv.getText());
                            } else {
                                showErrorDialog("Vui lòng nhập chuỗi IV");
                                break;
                            }
                        } else {    // nếu txtFieldIv không được bật thì không truyền IV
                            cipher.setIvParameterSpec((IvParameterSpec) null);
                        }
                        txtAreaOutput.setText(cipher.encryptBase64(text));
                    } catch (Exception ex) {
                        showErrorDialog(ex.getMessage());
                    }
                    break;
                }
                case "decrypt":{
                    String text = txtAreaInput.getText();
                    if(text.isEmpty()) {
                        showErrorDialog("Vui lòng nhập nội dung cần giải mã");
                        break;
                    }
                    // set key
                    try {
                        byte[] keyBytes = Base64.getDecoder().decode(txtAreaKey.getText());
                        SecretKey key = new SecretKeySpec(keyBytes, cipher.getName());
                        cipher.loadKey(key);
                    } catch (Exception ex){
                        showErrorDialog("Key không hợp lệ");
                        break;
                    }
                    // set mode, padding và decrypt
                    try {
                        String mode = cbbMode.getSelectedItem().toString();
                        String padding = cbbPadding.getSelectedItem().toString();
                        cipher.setCipher(mode, padding);
                        if(txtFieldIv.isEnabled()) {
                            if(!txtFieldIv.getText().isBlank()) {
                                cipher.setIvParameterSpec(txtFieldIv.getText());
                            } else {
                                showErrorDialog("Vui lòng nhập chuỗi IV");
                                break;
                            }
                        } else {
                            cipher.setIvParameterSpec((IvParameterSpec) null);
                        }
                        txtAreaOutput.setText(cipher.decryptBase64(text));
                    } catch (Exception ex) {
                        showErrorDialog(ex.getMessage());
                    }
                    break;
                }
                case "saveKey": {
                    if(txtAreaKey.getText().isEmpty()) {
                        showErrorDialog("Không thể lưu key rỗng");
                        break;
                    }
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setFileFilter(new FileNameExtensionFilter("DAT files (.dat)", "dat"));
                    fileChooser.setDialogTitle("Lưu key về máy tính");
                    int userOption = fileChooser.showSaveDialog(this.getParent());
                    if(userOption == JFileChooser.APPROVE_OPTION) {
                        File saveFile = fileChooser.getSelectedFile();
                        if(!saveFile.getAbsolutePath().endsWith(".dat")) {   // thêm đuôi .dat nếu chưa có
                            saveFile = new File(saveFile.getAbsolutePath() + ".dat");
                        }
                        // ghi key vào file
                        try {
                            FileOutputStream fos = new FileOutputStream(saveFile);
                            PrintWriter pw = new PrintWriter(new OutputStreamWriter(fos, "UTF-8"), true);
                            pw.println("KEY_SIZE\t" + cbbKeySize.getSelectedItem().toString());
                            pw.println("MODE\t" + cbbMode.getSelectedItem().toString());
                            pw.println("PADDING\t" + cbbPadding.getSelectedItem().toString());
                            pw.println("IV\t" + txtFieldIv.getText());
                            pw.println("KEY\t" + txtAreaKey.getText());
                            pw.close();
                        } catch (FileNotFoundException ex) {
                            showErrorDialog("Không tìm thấy file đích");
                        } catch (UnsupportedEncodingException ex) {
                            showErrorDialog("Kiểu encode không được hỗ trợ");
                        }
                    }
                    break;
                }
                case "loadKey": {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setFileFilter(new FileNameExtensionFilter("DAT files (.dat)", "dat"));
                    fileChooser.setDialogTitle("Tải key từ máy tính");
                    int userOption = fileChooser.showOpenDialog(this.getParent());
                    if(userOption == JFileChooser.APPROVE_OPTION) {
                        File saveFile = fileChooser.getSelectedFile();
                        if(!saveFile.getAbsolutePath().endsWith(".dat")) {  // báo lỗi nếu không phải file .dat
                            showErrorDialog("Chương trình chỉ hỗ trợ file .dat");
                            break;
                        }
                        // đọc key từ file
                        try {
                            FileInputStream fis = new FileInputStream(saveFile);
                            BufferedReader br = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
                            txtAreaKey.setText("");
                            String line;
                            while((line = br.readLine()) != null) {
                                String[] data = line.split("\t");
                                switch (data[0]) {
                                    case "KEY_SIZE": {
                                        cbbKeySize.setSelectedItem(Integer.parseInt(data[1]));
                                        break;
                                    }
                                    case "MODE": {
                                        cbbMode.setSelectedItem(data[1]);
                                        break;
                                    }
                                    case "PADDING": {
                                        cbbPadding.setSelectedItem(data[1]);
                                        break;
                                    }
                                    case "IV": {
                                        if(data.length == 2) txtFieldIv.setText(data[1]);
                                        break;
                                    }
                                    case "KEY": {
                                        txtAreaKey.setText(data[1]);
                                        break;
                                    }
                                }
                            }
                        } catch (FileNotFoundException ex) {
                            showErrorDialog("Không tìm thấy file đích");
                        } catch (UnsupportedEncodingException ex) {
                            showErrorDialog("Kiểu encode không được hỗ trợ");
                        } catch (IOException ex) {
                            showErrorDialog("Có lỗi xảy ra trong quá trình đọc file");
                        }
                    }
                    break;
                }
                case "chooseInput": {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setDialogTitle("Chọn file thao tác");
                    int userOption = fileChooser.showOpenDialog(SymmetricCipherView.this.getParent());
                    if(userOption == JFileChooser.APPROVE_OPTION) {
                        File inputFile = fileChooser.getSelectedFile();
                        txtFieldInput.setText(inputFile.getAbsolutePath());
                    }
                    break;
                }
                case "chooseOutput": {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setDialogTitle("Lưu file kết quả");
                    int userOption = fileChooser.showSaveDialog(SymmetricCipherView.this.getParent());
                    if(userOption == JFileChooser.APPROVE_OPTION) {
                        File outputFile = fileChooser.getSelectedFile();
                        txtFieldOutput.setText(outputFile.getAbsolutePath());
                    }
                    break;
                }
                case "encryptFile": {
                    // lấy input và output
                    String src = txtFieldInput.getText();
                    String dest = txtFieldOutput.getText();
                    // check path
                    File srcFile = new File(src);
                    if(!srcFile.exists() || !srcFile.isFile()) {
                        showErrorDialog("File nguồn không hợp lệ");
                        break;
                    }
                    File destFile = new File(dest);
                    if(destFile.getParentFile() == null || !destFile.getParentFile().exists()) {
                        showErrorDialog("File đích không hợp lệ");
                        break;
                    }
                    // set key
                    try {
                        byte[] keyBytes = Base64.getDecoder().decode(txtAreaKey.getText());
                        SecretKey key = new SecretKeySpec(keyBytes, cipher.getName());
                        cipher.loadKey(key);
                    } catch (Exception ex){
                        showErrorDialog("Key không hợp lệ");
                        break;
                    }
                    // set mode, padding và encrypt
                    try {
                        String mode = cbbMode.getSelectedItem().toString();
                        String padding = cbbPadding.getSelectedItem().toString();
                        cipher.setCipher(mode, padding);
                        if(txtFieldIv.isEnabled()) {    // nếu txtFieldIv được bật thì yêu cầu truyền IV
                            if(!txtFieldIv.getText().isBlank()) {
                                cipher.setIvParameterSpec(txtFieldIv.getText());
                            } else {
                                showErrorDialog("Vui lòng nhập chuỗi IV");
                                break;
                            }
                        } else {    // nếu txtFieldIv không được bật thì không truyền IV
                            cipher.setIvParameterSpec((IvParameterSpec) null);
                        }
                        // encrypt
                        boolean isEncrypted = cipher.encryptFile(src, dest);
                        if(isEncrypted) {
                            lblStatus.setText("Kết quả: Thao tác thành công");
                            lblResultPath.setText(txtFieldOutput.getText());
                            btnOpenFileLocation.setVisible(true);
                        } else {
                            lblStatus.setText("Kết quả: Thao tác không thành công");
                            showErrorDialog("Mã hóa không thành công");
                        }
                    } catch (Exception ex) {
                        showErrorDialog(ex.getMessage());
                    }
                    break;
                }
                case "decryptFile": {
                    // lấy input và output
                    String src = txtFieldInput.getText();
                    String dest = txtFieldOutput.getText();
                    // check path
                    File srcFile = new File(src);
                    if(!srcFile.exists() || !srcFile.isFile()) {
                        showErrorDialog("File nguồn không hợp lệ");
                        break;
                    }
                    File destFile = new File(dest);
                    if(!destFile.getParentFile().exists()) {
                        showErrorDialog("File đích không hợp lệ");
                        break;
                    }
                    // set key
                    try {
                        byte[] keyBytes = Base64.getDecoder().decode(txtAreaKey.getText());
                        SecretKey key = new SecretKeySpec(keyBytes, cipher.getName());
                        cipher.loadKey(key);
                    } catch (Exception ex){
                        showErrorDialog("Key không hợp lệ");
                        break;
                    }
                    // set mode, padding và encrypt
                    try {
                        String mode = cbbMode.getSelectedItem().toString();
                        String padding = cbbPadding.getSelectedItem().toString();
                        cipher.setCipher(mode, padding);
                        if(txtFieldIv.isEnabled()) {    // nếu txtFieldIv được bật thì yêu cầu truyền IV
                            if(!txtFieldIv.getText().isBlank()) {
                                cipher.setIvParameterSpec(txtFieldIv.getText());
                            } else {
                                showErrorDialog("Vui lòng nhập chuỗi IV");
                                break;
                            }
                        } else {    // nếu txtFieldIv không được bật thì không truyền IV
                            cipher.setIvParameterSpec((IvParameterSpec) null);
                        }
                        // decrypt
                        boolean isDecrypted = cipher.decryptFile(src, dest);
                        if(isDecrypted) {
                            lblStatus.setText("Kết quả: Thao tác thành công");
                            lblResultPath.setText(txtFieldOutput.getText());
                            btnOpenFileLocation.setVisible(true);
                        } else {
                            lblStatus.setText("Kết quả: Thao tác không thành công");
                            showErrorDialog("Giải mã không thành công");
                        }
                    } catch (Exception ex) {
                        showErrorDialog(ex.getMessage());
                    }
                    break;
                }
                case "openFileLocation": {
                    File resultFile = new File(lblResultPath.getText());
                    File container = resultFile.getParentFile();
                    if(Desktop.isDesktopSupported()) {
                        Desktop desktop = Desktop.getDesktop();
                        try {
                            desktop.open(container);
                        } catch (IOException ex) {
                            showErrorDialog("Không mở được thư mục");
                        }
                    } else {
                        showErrorDialog("Hệ điều hành bạn đang sử dụng không hỗ trợ thao tác này");
                    }
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