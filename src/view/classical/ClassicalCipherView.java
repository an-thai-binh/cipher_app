package view.classical;

import model.classical.*;
import utils.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

public class ClassicalCipherView extends JPanel implements ActionListener {
    private final IClassicalCipher cipher;
    private JTextArea txtAreaInput, txtAreaKey, txtAreaOutput;
    private JButton btnCopyKey, btnEncrypt, btnDecrypt;
    private JRadioButton rdbEnglish, rdbVietnam, rdbSize2, rdbSize3, rdbTextFormat, rdbMatrixFormat;
    public ClassicalCipherView(IClassicalCipher cipher) {
        this.cipher = cipher;
        if(!(cipher instanceof HillCipher))
            init();
        else
            initHill();
    }

    /**
     * init hiển thị giao diện mã hóa cổ điển
     */
    private void init() {
        renderSelf();
        renderTitleRow();
        renderLanguageRow();
        renderInputRow();
        renderKeyRow();
        renderActionRow();
        renderOutputRow();
    }

    /**
     * initHill hiển thị giao diện mã hóa Hill
     */
    private void initHill() {
        renderSelf();
        renderTitleRow();
        renderLanguageRow();
        renderInputRow();
        renderKeySizeRow();
        renderKeyFormatRow();
        renderKeyRow();
        renderActionRow();
        renderOutputRow();
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
        lblCipherTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        lblCipherTitle.setAlignmentX(Component.CENTER_ALIGNMENT);   // center trong boxlayout
        this.add(lblCipherTitle);
    }

    /**
     * renderLanguageRow    cài đặt giao diện dòng chọn ngôn ngữ
     */
    private void renderLanguageRow() {
        JPanel pnlLanguage = new JPanel(new GridLayout(1, 3));
        pnlLanguage.setBackground(null);
        JPanel pnlLanguageChoose = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlLanguageChoose.setBackground(null);
        pnlLanguageChoose.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        JLabel lblLanguageChoose = new JLabel("Chọn ngôn ngữ:");
        lblLanguageChoose.setFont(FontUtils.createRobotoFont("medium", 16f));
        pnlLanguageChoose.add(lblLanguageChoose);
        ButtonGroup btnGroupLanguage = new ButtonGroup();
        // ô tiếng Anh
        JPanel pnlEnglishBox = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlEnglishBox.setBackground(null);
        rdbEnglish = new JRadioButton();
        rdbEnglish.addActionListener(this);
        rdbEnglish.setBackground(null);
        rdbEnglish.setIcon(IconUtils.RDB_UNCHECK_ICON);
        rdbEnglish.setSelectedIcon(IconUtils.RDB_CHECKED_ICON);
        rdbEnglish.setRolloverIcon(IconUtils.RDB_ROLLOVER_ICON);
        rdbEnglish.setSelected(true);
        btnGroupLanguage.add(rdbEnglish);
        JLabel lblEnglishIcon = new JLabel(IconUtils.ENGLISH_FLAG_ICON);
        JLabel lblEnglish = new JLabel("English", JLabel.CENTER);
        lblEnglish.setFont(FontUtils.createRobotoFont("regular", 16f));
        pnlEnglishBox.add(rdbEnglish);
        pnlEnglishBox.add(lblEnglishIcon);
        pnlEnglishBox.add(lblEnglish);
        // ô tiếng Việt
        JPanel pnlVietnamBox = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlVietnamBox.setBackground(null);
        rdbVietnam = new JRadioButton();
        rdbVietnam.addActionListener(this);
        rdbVietnam.setBackground(null);
        rdbVietnam.setIcon(IconUtils.RDB_UNCHECK_ICON);
        rdbVietnam.setSelectedIcon(IconUtils.RDB_CHECKED_ICON);
        rdbVietnam.setRolloverIcon(IconUtils.RDB_ROLLOVER_ICON);
        btnGroupLanguage.add(rdbVietnam);
        JLabel lblVietnamIcon = new JLabel(IconUtils.VIETNAMESE_FLAG_ICON);
        JLabel lblVietnam = new JLabel("Tiếng Việt", JLabel.CENTER);
        lblVietnam.setFont(FontUtils.createRobotoFont("regular", 16f));
        pnlVietnamBox.add(rdbVietnam);
        pnlVietnamBox.add(lblVietnamIcon);
        pnlVietnamBox.add(lblVietnam);
        // thêm vào panel
        pnlLanguage.add(pnlLanguageChoose);
        pnlLanguage.add(pnlEnglishBox);
        pnlLanguage.add(pnlVietnamBox);
        this.add(pnlLanguage);
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
        inputScrollPane.setPreferredSize(new Dimension(520, 185));
        inputScrollPane.setBackground(null);
        this.add(inputScrollPane);
    }

    /**
     * renderKeySizeRow cài đặt giao diện dòng chọn kích thuớc key
     */
    private void renderKeySizeRow() {
        JPanel pnlKeySize = new JPanel(new GridLayout(1, 3));
        pnlKeySize.setPreferredSize(new Dimension(520, 50));
        pnlKeySize.setBackground(null);
        JPanel pnlKeySizeChoose = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlKeySizeChoose.setBackground(null);
        pnlKeySizeChoose.setBorder(BorderFactory.createEmptyBorder(10, 0, 0 ,0));
        JLabel lblKeySizeChoose = new JLabel("Kích thước key:");
        lblKeySizeChoose.setFont(FontUtils.createRobotoFont("medium", 16f));
        pnlKeySizeChoose.add(lblKeySizeChoose);
        ButtonGroup btnGroupKeySize = new ButtonGroup();
        // ô 2x2
        JPanel pnlSize2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlSize2.setBackground(null);
        rdbSize2 = new JRadioButton();
        rdbSize2.addActionListener(this);
        rdbSize2.setBackground(null);
        rdbSize2.setIcon(IconUtils.RDB_UNCHECK_ICON);
        rdbSize2.setSelectedIcon(IconUtils.RDB_CHECKED_ICON);
        rdbSize2.setRolloverIcon(IconUtils.RDB_ROLLOVER_ICON);
        rdbSize2.setSelected(true);
        btnGroupKeySize.add(rdbSize2);
        JLabel lblSize2 = new JLabel("2x2", JLabel.CENTER);
        lblSize2.setFont(FontUtils.createRobotoFont("regular", 16f));
        pnlSize2.add(rdbSize2);
        pnlSize2.add(lblSize2);
        // ô 3x3
        JPanel pnlSize3 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlSize3.setBackground(null);
        rdbSize3 = new JRadioButton();
        rdbSize3.addActionListener(this);
        rdbSize3.setBackground(null);
        rdbSize3.setIcon(IconUtils.RDB_UNCHECK_ICON);
        rdbSize3.setSelectedIcon(IconUtils.RDB_CHECKED_ICON);
        rdbSize3.setRolloverIcon(IconUtils.RDB_ROLLOVER_ICON);
        btnGroupKeySize.add(rdbSize3);
        JLabel lblSize3 = new JLabel("3x3", JLabel.CENTER);
        lblSize3.setFont(FontUtils.createRobotoFont("regular", 16f));
        pnlSize3.add(rdbSize3);
        pnlSize3.add(lblSize3);

        pnlKeySize.add(pnlKeySizeChoose);
        pnlKeySize.add(pnlSize2);
        pnlKeySize.add(pnlSize3);
        this.add(pnlKeySize);
    }

    /**
     * renderKeyFormatRow   cài đặt giao diện dòng chọn định dạng key
     */
    private void renderKeyFormatRow() {
        JPanel pnlFormatKey = new JPanel(new GridLayout(1, 3));
        pnlFormatKey.setPreferredSize(new Dimension(520, 50));
        pnlFormatKey.setBackground(null);
        JPanel pnlKeyFormatChoose = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlKeyFormatChoose.setBackground(null);
        pnlKeyFormatChoose.setBorder(BorderFactory.createEmptyBorder(10, 0, 0 ,0));
        JLabel lblKeyFormatChoose = new JLabel("Định dạng key:");
        lblKeyFormatChoose.setFont(FontUtils.createRobotoFont("medium", 16f));
        pnlKeyFormatChoose.add(lblKeyFormatChoose);
        ButtonGroup btnGroupKeyFormat = new ButtonGroup();
        // dạng chuỗi ký tự
        JPanel pnlTextFormat = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlTextFormat.setBackground(null);
        rdbTextFormat = new JRadioButton();
        rdbTextFormat.setBackground(null);
        rdbTextFormat.setIcon(IconUtils.RDB_UNCHECK_ICON);
        rdbTextFormat.setSelectedIcon(IconUtils.RDB_CHECKED_ICON);
        rdbTextFormat.setRolloverIcon(IconUtils.RDB_ROLLOVER_ICON);
        rdbTextFormat.setSelected(true);
        btnGroupKeyFormat.add(rdbTextFormat);
        JLabel lblTextFormat = new JLabel("Chuỗi ký tự", JLabel.CENTER);
        lblTextFormat.setFont(FontUtils.createRobotoFont("regular", 16f));
        pnlTextFormat.add(rdbTextFormat);
        pnlTextFormat.add(lblTextFormat);
        // dạng ma trận
        JPanel pnlMatrixFormat = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlMatrixFormat.setBackground(null);
        rdbMatrixFormat = new JRadioButton();
        rdbMatrixFormat.setBackground(null);
        rdbMatrixFormat.setIcon(IconUtils.RDB_UNCHECK_ICON);
        rdbMatrixFormat.setSelectedIcon(IconUtils.RDB_CHECKED_ICON);
        rdbMatrixFormat.setRolloverIcon(IconUtils.RDB_ROLLOVER_ICON);
        btnGroupKeyFormat.add(rdbMatrixFormat);
        JLabel lblMatrixFormat = new JLabel("Ma trận số", JLabel.CENTER);
        lblMatrixFormat.setFont(FontUtils.createRobotoFont("regular", 16f));
        pnlMatrixFormat.add(rdbMatrixFormat);
        pnlMatrixFormat.add(lblMatrixFormat);

        pnlFormatKey.add(pnlKeyFormatChoose);
        pnlFormatKey.add(pnlTextFormat);
        pnlFormatKey.add(pnlMatrixFormat);
        this.add(pnlFormatKey);
    }

    /**
     * renderKeyRow cài đặt giao diện dòng key
     */
    private void renderKeyRow() {
        JPanel pnlKey = new JPanel();
        pnlKey.setLayout(new BoxLayout(pnlKey, BoxLayout.X_AXIS));
        pnlKey.setBorder(BorderFactory.createEmptyBorder(10, 0 ,0 ,0));
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
        btnGenKey.setBackground(Color.decode("#D9D9D9"));
        btnGenKey.setFont(FontUtils.createRobotoFont("regular", 20f));
        btnGenKey.setPreferredSize(new Dimension(128, 44));
        btnCopyKey = new JButton("Sao chép");
        btnCopyKey.setActionCommand("copy");
        btnCopyKey.addActionListener(this);
        btnCopyKey.setBackground(Color.decode("#D9D9D9"));
        btnCopyKey.setFont(FontUtils.createRobotoFont("regular", 20f));
        JPanel pnlSaveLoad = new JPanel(new GridLayout(1, 2));
        JButton btnSaveKey = new JButton(IconUtils.SAVE_ICON);
        btnSaveKey.setActionCommand("saveKey");
        btnSaveKey.addActionListener(this);
        btnSaveKey.setPreferredSize(new Dimension(64, 44));
        btnSaveKey.setBackground(Color.decode("#D9D9D9"));
        btnSaveKey.setToolTipText("Lưu key về máy dưới dạng file .dat");
        JButton btnLoadKey = new JButton(IconUtils.LOAD_ICON);
        btnLoadKey.setActionCommand("loadKey");
        btnLoadKey.addActionListener(this);
        btnLoadKey.setPreferredSize(new Dimension(64, 44));
        btnLoadKey.setBackground(Color.decode("#D9D9D9"));
        btnLoadKey.setToolTipText("Tải key lên từ file .dat");

        // thêm vào panel
        pnlSaveLoad.add(btnSaveKey);
        pnlSaveLoad.add(btnLoadKey);
        pnlKeyTool.add(btnCopyKey);
        pnlKeyTool.add(btnGenKey);
        pnlKeyTool.add(pnlSaveLoad);
        pnlKey.add(pnlKeyTool);
        this.add(pnlKey);
    }

    /**
     * renderActionRow  cài đặt giao diện dòng action
     */
    private void renderActionRow() {
        JPanel pnlAction = new JPanel(new GridLayout(1, 2));
        pnlAction.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        pnlAction.setBackground(null);
        btnEncrypt = new JButton("Mã hóa");
        btnEncrypt.setActionCommand("encrypt");
        btnEncrypt.addActionListener(this);
        btnEncrypt.setBackground(Color.decode("#F3C623"));
        btnEncrypt.setFont(FontUtils.createRobotoFont("medium", 24f));
        btnDecrypt = new JButton("Giải mã");
        btnDecrypt.setActionCommand("decrypt");
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

    @Override
    public void actionPerformed(ActionEvent e) {
        // xử lý sự kiện cho các radio button
        if(e.getSource() instanceof JRadioButton) {
            // chọn ngôn ngữ
            try {
                if(rdbEnglish.isSelected()) {
                    cipher.setAlphabet(LanguageSupport.EN);
                } else {
                    cipher.setAlphabet(LanguageSupport.VI);
                }
            } catch (Exception ex) {
                showErrorDialog("Ngôn ngữ không được hỗ trợ");
            }
            // chọn bậc ma trận
            if(cipher instanceof HillCipher) {
                try {
                    if (rdbSize2.isSelected()) {
                        cipher.setOrder(2);
                    } else {
                        cipher.setOrder(3);
                    }
                } catch (Exception ex) {
                    showErrorDialog("Kích thước ma trận không được hỗ trợ");
                }
            }
        }

        // xử lý sự kiện cho các button
        String cmd = e.getActionCommand();
        switch (cmd) {
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
            case "genKey": {    // sự kiện tạo key
                Object o = cipher.genKey();
                if(cipher instanceof AffineCipher) {    // affine key
                    AffineKey key = (AffineKey) o;
                    txtAreaKey.setText(key.getA() + "-" + key.getB());
                } else if(cipher instanceof HillCipher) {   // hill key
                    HillKey hill = (HillKey) o;
                    if(rdbTextFormat.isSelected()) {    // key dạng text
                        txtAreaKey.setText(hill.getKeyText());
                    } else {    // key dạng ma trận
                        int[][] key = hill.getKey();
                        txtAreaKey.setText(Array2DUtils.toString(key));
                    }
                } else {    // còn lại
                    String key = o.toString();
                    txtAreaKey.setText(key);
                }
                break;
            }
            case "encrypt": {   // sự kiện mã hóa
                String text = txtAreaInput.getText();
                if(text.isEmpty()) {
                    showErrorDialog("Vui lòng nhập nội dung cần mã hóa");
                    break;
                }
                try {
                    if(cipher instanceof HillCipher) {  // hill
                        if(rdbTextFormat.isSelected()) {
                            cipher.loadKey(txtAreaKey.getText());
                        } else {
                            int[][] key = Array2DUtils.toInt2DArray(txtAreaKey.getText());
                            cipher.loadKey(key);
                        }
                    } else {    // còn lại
                        cipher.loadKey(txtAreaKey.getText());
                    }
                    String encryptedText = cipher.encrypt(text);
                    txtAreaOutput.setText(encryptedText);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    showErrorDialog(ex.getMessage());
                }
                break;
            }
            case "decrypt": {   // sự kiện giải mã
                String text = txtAreaInput.getText();
                if(text.isEmpty()) {
                    showErrorDialog("Vui lòng nhập nội dung cần giải mã");
                    break;
                }
                try {
                    if(cipher instanceof HillCipher) {  // hill
                        if(rdbTextFormat.isSelected()) {
                            cipher.loadKey(txtAreaKey.getText());
                        } else {
                            int[][] key = Array2DUtils.toInt2DArray(txtAreaKey.getText());
                            cipher.loadKey(key);
                        }
                    } else {    // còn lại
                        cipher.loadKey(txtAreaKey.getText());
                    }
                    String decryptedText = cipher.decrypt(text);
                    txtAreaOutput.setText(decryptedText);
                } catch (Exception ex) {
                    showErrorDialog(ex.getMessage());
                }
                break;
            }
            case "saveKey": {   // sự kiện save key xuống file
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
                        pw.write(txtAreaKey.getText());
                        pw.close();
                    } catch (FileNotFoundException ex) {
                        showErrorDialog("Không tìm thấy file đích");
                    } catch (UnsupportedEncodingException ex) {
                        showErrorDialog("Kiểu encode không được hỗ trợ");
                    }
                }
                break;
            }
            case "loadKey": {   // sự kiện load key từ file
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
                        ArrayList<String> lineList = new ArrayList<>();
                        while((line = br.readLine()) != null) {
                            lineList.add(line);
                        }
                        for(int i = 0; i < lineList.size(); i++) {
                            txtAreaKey.append(lineList.get(i));
                            if(i < lineList.size() - 1) txtAreaKey.append("\n");    // chỉ thêm \n ở dòng cuối cùng
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
