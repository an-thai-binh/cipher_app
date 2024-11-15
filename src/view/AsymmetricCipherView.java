package view;

import model.asymmetric.AsymmetricCipher;
import utils.FontUtils;
import utils.IconUtils;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.security.KeyPair;
import java.util.Base64;

public class AsymmetricCipherView extends JPanel implements ActionListener {
    private JComboBox cbbKeySize;
    private JTextArea txtAreaPublicKey, txtAreaPrivateKey;
    private JButton btnCopyPublicKey, btnCopyPrivateKey;
    private final AsymmetricCipher cipher;
    public AsymmetricCipherView(AsymmetricCipher cipher) {
        this.cipher = cipher;
        init();
    }

    private void init() {
        renderSelf();
        renderKeyTool();
        renderMain();
    }

    /**
     * renderSelf   cài đặt giao diện chính
     */
    private void renderSelf() {
        this.setLayout(new BorderLayout());
        this.setBackground(Color.decode("#F4F6FF"));
    }

    /**
     * renderKeyTool    cài đặt giao diện khung công cụ tạo khóa
     */
    private void renderKeyTool() {
        JPanel pnlKeyTool = new JPanel();
        pnlKeyTool.setLayout(new BoxLayout(pnlKeyTool, BoxLayout.Y_AXIS));
        pnlKeyTool.setPreferredSize(new Dimension(300, 0));
        pnlKeyTool.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        // key tool title
        JLabel lblKeyToolTitle = new JLabel("CÔNG CỤ TẠO KEY", JLabel.CENTER);
        lblKeyToolTitle.setFont(FontUtils.createRobotoFont("extraBold", 20f));
        lblKeyToolTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlKeyTool.add(lblKeyToolTitle);
        // key size
        JPanel pnlKeySize = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlKeySize.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        JLabel lblKeySize = new JLabel("Kích thước", JLabel.CENTER);
        lblKeySize.setFont(FontUtils.createRobotoFont("medium", 16f));
        cbbKeySize = new JComboBox(cipher.getSupportedKeySize().toArray(new Integer[0]));
        cbbKeySize.setPreferredSize(new Dimension(100, 30));
        cbbKeySize.setBackground(Color.WHITE);
        cbbKeySize.setRenderer(new DefaultListCellRenderer() {
            @Override
            public void paint(Graphics g) {
                setFont(FontUtils.createRobotoFont("medium", 20f));
                setHorizontalAlignment(DefaultListCellRenderer.CENTER); // center item
                super.paint(g);
            }
        });
        pnlKeySize.add(lblKeySize);
        pnlKeySize.add(cbbKeySize);
        pnlKeyTool.add(pnlKeySize);
        // gen key button
        JPanel pnlGenKey = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnGenKey = new JButton("Tạo bộ key");
        btnGenKey.setActionCommand("genKey");
        btnGenKey.addActionListener(this);
        btnGenKey.setFont(FontUtils.createRobotoFont("medium", 16f));
        pnlGenKey.add(btnGenKey);
        pnlKeyTool.add(pnlGenKey);
        // public key area
        JPanel pnlPublicKey = new JPanel(new BorderLayout());
        pnlPublicKey.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        txtAreaPublicKey = new JTextArea();   // text area
        txtAreaPublicKey.setLineWrap(true);
        txtAreaPublicKey.setWrapStyleWord(true);
        txtAreaPublicKey.setFont(FontUtils.createRobotoFont("regular", 16f));
        txtAreaPublicKey.setForeground(Color.black);
        TitledBorder borderPublic = BorderFactory.createTitledBorder("Public key");
        borderPublic.setTitleFont(FontUtils.createRobotoFont("regular", 13f));
        txtAreaPublicKey.setBorder(borderPublic);
        JScrollPane publicKeyScrollPane = new JScrollPane(txtAreaPublicKey);
        publicKeyScrollPane.setPreferredSize(new Dimension(0, 200));
        publicKeyScrollPane.setBackground(null);
        pnlPublicKey.add(publicKeyScrollPane, BorderLayout.CENTER);
        JPanel pnlPublicKeyTool = new JPanel(new GridLayout(1, 2)); // key tool
        btnCopyPublicKey = new JButton("Sao chép");
        btnCopyPublicKey.setActionCommand("copyPublicKey");
        btnCopyPublicKey.addActionListener(this);
        btnCopyPublicKey.setFont(FontUtils.createRobotoFont("medium", 16f));
        JButton btnSavePublicKey = new JButton("Lưu");
        btnSavePublicKey.setActionCommand("savePublicKey");
        btnSavePublicKey.addActionListener(this);
        btnSavePublicKey.setFont(FontUtils.createRobotoFont("medium", 16f));
        pnlPublicKeyTool.add(btnCopyPublicKey);
        pnlPublicKeyTool.add(btnSavePublicKey);
        pnlPublicKey.add(pnlPublicKeyTool, BorderLayout.SOUTH);
        pnlKeyTool.add(pnlPublicKey);   // add
        // private key area
        JPanel pnlPrivateKey = new JPanel(new BorderLayout());
        pnlPrivateKey.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        txtAreaPrivateKey = new JTextArea();   // text area
        txtAreaPrivateKey.setLineWrap(true);
        txtAreaPrivateKey.setWrapStyleWord(true);
        txtAreaPrivateKey.setFont(FontUtils.createRobotoFont("regular", 16f));
        txtAreaPrivateKey.setForeground(Color.black);
        TitledBorder borderPrivate = BorderFactory.createTitledBorder("Private key");
        borderPrivate.setTitleFont(FontUtils.createRobotoFont("regular", 13f));
        txtAreaPrivateKey.setBorder(borderPrivate);
        JScrollPane privateKeyScrollPane = new JScrollPane(txtAreaPrivateKey);
        privateKeyScrollPane.setPreferredSize(new Dimension(0, 200));
        privateKeyScrollPane.setBackground(null);
        pnlPrivateKey.add(privateKeyScrollPane, BorderLayout.CENTER);
        JPanel pnlPrivateKeyTool = new JPanel(new GridLayout(1, 2)); // key tool
        btnCopyPrivateKey = new JButton("Sao chép");
        btnCopyPrivateKey.setActionCommand("copyPrivateKey");
        btnCopyPrivateKey.addActionListener(this);
        btnCopyPrivateKey.setFont(FontUtils.createRobotoFont("medium", 16f));
        JButton btnSavePrivateKey = new JButton("Lưu");
        btnSavePrivateKey.setActionCommand("savePrivateKey");
        btnSavePrivateKey.addActionListener(this);
        btnSavePrivateKey.setFont(FontUtils.createRobotoFont("medium", 16f));
        pnlPrivateKeyTool.add(btnCopyPrivateKey);
        pnlPrivateKeyTool.add(btnSavePrivateKey);
        pnlPrivateKey.add(pnlPrivateKeyTool, BorderLayout.SOUTH);
        pnlKeyTool.add(pnlPrivateKey);   // add
        this.add(pnlKeyTool, BorderLayout.WEST);
    }

    /**
     * renderMain cài đặt giao diện khu vực chính
     */
    private void renderMain() {
        JPanel pnlMain = new JPanel();
        pnlMain.setLayout(new BoxLayout(pnlMain, BoxLayout.Y_AXIS));
        pnlMain.setBackground(Color.decode("#F4F6FF"));
        // title
        JLabel lblCipherTitle = new JLabel(cipher.getName(), JLabel.CENTER);
        lblCipherTitle.setFont(FontUtils.createRobotoFont("extraBold", 32f));
        lblCipherTitle.setForeground(Color.BLACK);
        lblCipherTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        lblCipherTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlMain.add(lblCipherTitle);
        // cipher mode
        JPanel pnlCipherMode = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlCipherMode.setBackground(null);
        JLabel lblCipherMode = new JLabel("Chọn chế độ:", JLabel.CENTER);
        lblCipherMode.setFont(FontUtils.createRobotoFont("medium", 16f));
        pnlCipherMode.add(lblCipherMode);
        JComboBox cbbCipherMode = new JComboBox(cipher.getSupportedInstace().toArray(new String[0]));
        cbbCipherMode.setBackground(Color.WHITE);
        cbbCipherMode.setPreferredSize(new Dimension(300, 30));
        cbbCipherMode.setRenderer(new DefaultListCellRenderer() {
            @Override
            public void paint(Graphics g) {
                setFont(FontUtils.createRobotoFont("medium", 20f));
                setHorizontalAlignment(DefaultListCellRenderer.CENTER); // center item
                super.paint(g);
            }
        });
        pnlCipherMode.add(cbbCipherMode);
        pnlMain.add(pnlCipherMode);
        // action section
        JPanel pnlAction = new JPanel(new GridLayout(1, 2));
        pnlAction.setBackground(null);
        renderEncryptSide(pnlAction);
        renderDecryptSide(pnlAction);
        pnlMain.add(pnlAction);
        this.add(pnlMain, BorderLayout.CENTER);
    }

    /**
     * renderEncryptSide    cài đặt khu vực mã hóa
     * @param panel parent
     */
    private void renderEncryptSide(JPanel panel) {
        JPanel pnlEncrypt = new JPanel();
        pnlEncrypt.setBackground(null);
        pnlEncrypt.setLayout(new BoxLayout(pnlEncrypt, BoxLayout.Y_AXIS));
        pnlEncrypt.setBorder(BorderFactory.createEmptyBorder(0, 10, 20, 10));
        // title
        JLabel lblEncryptTitle = new JLabel("Mã hóa", JLabel.CENTER);
        lblEncryptTitle.setFont(FontUtils.createRobotoFont("medium", 16));
        lblEncryptTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblEncryptTitle.setBorder(BorderFactory.createEmptyBorder(0,0, 10, 0));
        pnlEncrypt.add(lblEncryptTitle);
        // input
        JTextArea txtAreaInputEncrypt = new JTextArea();   // text area
        txtAreaInputEncrypt.setLineWrap(true);
        txtAreaInputEncrypt.setWrapStyleWord(true);
        txtAreaInputEncrypt.setFont(FontUtils.createRobotoFont("regular", 16f));
        txtAreaInputEncrypt.setForeground(Color.black);
        TitledBorder borderInput = BorderFactory.createTitledBorder("Nhập văn bản cần mã hóa");
        borderInput.setTitleFont(FontUtils.createRobotoFont("regular", 13f));
        txtAreaInputEncrypt.setBorder(borderInput);
        JScrollPane inputEncryptScrollPane = new JScrollPane(txtAreaInputEncrypt);
        inputEncryptScrollPane.setPreferredSize(new Dimension(0, 200));
        inputEncryptScrollPane.setBackground(null);
        pnlEncrypt.add(inputEncryptScrollPane);
        // key
        renderEncryptKeyRow(pnlEncrypt);
        // action button
        JPanel pnlEncryptAction = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlEncryptAction.setBackground(null);
        JButton btnEncrypt = new JButton("Mã hóa");
        btnEncrypt.setActionCommand("encrypt");
        btnEncrypt.setPreferredSize(new Dimension(200, 40));
        btnEncrypt.setBackground(Color.decode("#F3C623"));
        btnEncrypt.setFont(FontUtils.createRobotoFont("medium", 24f));
        pnlEncryptAction.add(btnEncrypt);
        pnlEncrypt.add(pnlEncryptAction);
        // output
        JTextArea txtAreaOutputEncrypt = new JTextArea();   // text area
        txtAreaOutputEncrypt.setEditable(false);
        txtAreaOutputEncrypt.setLineWrap(true);
        txtAreaOutputEncrypt.setWrapStyleWord(true);
        txtAreaOutputEncrypt.setFont(FontUtils.createRobotoFont("regular", 16f));
        txtAreaOutputEncrypt.setForeground(Color.black);
        TitledBorder borderOutput = BorderFactory.createTitledBorder("Kết quả");
        borderOutput.setTitleFont(FontUtils.createRobotoFont("regular", 13f));
        txtAreaOutputEncrypt.setBorder(borderOutput);
        JScrollPane outputEncryptScrollPane = new JScrollPane(txtAreaOutputEncrypt);
        outputEncryptScrollPane.setPreferredSize(new Dimension(0, 200));
        outputEncryptScrollPane.setBackground(null);
        pnlEncrypt.add(outputEncryptScrollPane);
        
        panel.add(pnlEncrypt);
    }

    /**
     * renderDecryptSide    cài đặt khu vực giải mã
     * @param panel parent
     */
    private void renderDecryptSide(JPanel panel) {
        JPanel pnlDecrypt = new JPanel();
        pnlDecrypt.setBackground(null);
        pnlDecrypt.setLayout(new BoxLayout(pnlDecrypt, BoxLayout.Y_AXIS));
        pnlDecrypt.setBorder(BorderFactory.createEmptyBorder(0, 10, 20, 10));
        // title
        JLabel lblDecryptTitle = new JLabel("Giải mã", JLabel.CENTER);
        lblDecryptTitle.setFont(FontUtils.createRobotoFont("medium", 16));
        lblDecryptTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblDecryptTitle.setBorder(BorderFactory.createEmptyBorder(0,0, 10, 0));
        pnlDecrypt.add(lblDecryptTitle);
        // input
        JTextArea txtAreaInputDecrypt = new JTextArea();   // text area
        txtAreaInputDecrypt.setLineWrap(true);
        txtAreaInputDecrypt.setWrapStyleWord(true);
        txtAreaInputDecrypt.setFont(FontUtils.createRobotoFont("regular", 16f));
        txtAreaInputDecrypt.setForeground(Color.black);
        TitledBorder borderInput = BorderFactory.createTitledBorder("Nhập văn bản cần giải mã");
        borderInput.setTitleFont(FontUtils.createRobotoFont("regular", 13f));
        txtAreaInputDecrypt.setBorder(borderInput);
        JScrollPane inputDecryptScrollPane = new JScrollPane(txtAreaInputDecrypt);
        inputDecryptScrollPane.setPreferredSize(new Dimension(0, 200));
        inputDecryptScrollPane.setBackground(null);
        pnlDecrypt.add(inputDecryptScrollPane);
        // key
        renderDecryptKeyRow(pnlDecrypt);
        // action button
        JPanel pnlDecryptAction = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlDecryptAction.setBackground(null);
        JButton btnDecrypt = new JButton("Giải mã");
        btnDecrypt.setActionCommand("decrypt");
        btnDecrypt.setPreferredSize(new Dimension(200, 40));
        btnDecrypt.setBackground(Color.decode("#F3C623"));
        btnDecrypt.setFont(FontUtils.createRobotoFont("medium", 24f));
        pnlDecryptAction.add(btnDecrypt);
        pnlDecrypt.add(pnlDecryptAction);
        // output
        JTextArea txtAreaOutputDecrypt = new JTextArea();   // text area
        txtAreaOutputDecrypt.setEditable(false);
        txtAreaOutputDecrypt.setLineWrap(true);
        txtAreaOutputDecrypt.setWrapStyleWord(true);
        txtAreaOutputDecrypt.setFont(FontUtils.createRobotoFont("regular", 16f));
        txtAreaOutputDecrypt.setForeground(Color.black);
        TitledBorder borderOutput = BorderFactory.createTitledBorder("Kết quả");
        borderOutput.setTitleFont(FontUtils.createRobotoFont("regular", 13f));
        txtAreaOutputDecrypt.setBorder(borderOutput);
        JScrollPane outputDecryptScrollPane = new JScrollPane(txtAreaOutputDecrypt);
        outputDecryptScrollPane.setPreferredSize(new Dimension(0, 200));
        outputDecryptScrollPane.setBackground(null);
        pnlDecrypt.add(outputDecryptScrollPane);

        panel.add(pnlDecrypt);
    }

    private void renderEncryptKeyRow(JPanel panel) {
        JPanel pnlKey = new JPanel();
        pnlKey.setLayout(new BoxLayout(pnlKey, BoxLayout.X_AXIS));
        pnlKey.setBorder(BorderFactory.createEmptyBorder(10, 0 ,0 ,0));
        pnlKey.setBackground(null);

        // key icon
        JPanel pnlKeyIcon = new JPanel(new GridLayout(1, 1));
        pnlKeyIcon.setPreferredSize(new Dimension(74, 74));
        pnlKeyIcon.setBackground(Color.decode("#EB8317"));
        pnlKeyIcon.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JLabel lblKeyIcon = new JLabel(IconUtils.KEY_ICON, JLabel.CENTER);
        pnlKeyIcon.add(lblKeyIcon);
        pnlKey.add(pnlKeyIcon);

        // key text area
        JTextArea txtAreaEncryptKey = new JTextArea();
        txtAreaEncryptKey.setFont(FontUtils.createRobotoFont("regular", 16f));
        txtAreaEncryptKey.setLineWrap(true);
        txtAreaEncryptKey.setWrapStyleWord(true);
        JScrollPane keyScrollPane = new JScrollPane(txtAreaEncryptKey);
        keyScrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        keyScrollPane.setPreferredSize(new Dimension(402, 74));
        pnlKey.add(keyScrollPane);

        // key tool
        JPanel pnlKeyTool = new JPanel(new GridLayout(1, 1));
        pnlKeyTool.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JButton btnLoadEncryptKey = new JButton(IconUtils.LOAD_ICON);
        btnLoadEncryptKey.setActionCommand("loadKey");
//        btnLoadEncryptKey.addActionListener(this);
        btnLoadEncryptKey.setPreferredSize(new Dimension(64, 44));
        btnLoadEncryptKey.setBackground(Color.decode("#D9D9D9"));
        btnLoadEncryptKey.setToolTipText("Tải key lên từ file .dat");
        pnlKeyTool.add(btnLoadEncryptKey);

        // thêm vào panel
        pnlKey.add(pnlKeyTool);
        panel.add(pnlKey);
    }

    private void renderDecryptKeyRow(JPanel panel) {
        JPanel pnlKey = new JPanel();
        pnlKey.setLayout(new BoxLayout(pnlKey, BoxLayout.X_AXIS));
        pnlKey.setBorder(BorderFactory.createEmptyBorder(10, 0 ,0 ,0));
        pnlKey.setBackground(null);

        // key icon
        JPanel pnlKeyIcon = new JPanel(new GridLayout(1, 1));
        pnlKeyIcon.setPreferredSize(new Dimension(74, 74));
        pnlKeyIcon.setBackground(Color.decode("#EB8317"));
        pnlKeyIcon.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JLabel lblKeyIcon = new JLabel(IconUtils.KEY_ICON, JLabel.CENTER);
        pnlKeyIcon.add(lblKeyIcon);
        pnlKey.add(pnlKeyIcon);

        // key text area
        JTextArea txtAreaDecryptKey = new JTextArea();
        txtAreaDecryptKey.setFont(FontUtils.createRobotoFont("regular", 16f));
        txtAreaDecryptKey.setLineWrap(true);
        txtAreaDecryptKey.setWrapStyleWord(true);
        JScrollPane keyScrollPane = new JScrollPane(txtAreaDecryptKey);
        keyScrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        keyScrollPane.setPreferredSize(new Dimension(402, 74));
        pnlKey.add(keyScrollPane);

        // key tool
        JPanel pnlKeyTool = new JPanel(new GridLayout(1, 1));
        pnlKeyTool.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JButton btnLoadDecryptKey = new JButton(IconUtils.LOAD_ICON);
        btnLoadDecryptKey.setActionCommand("loadKey");
//        btnLoadDecryptKey.addActionListener(this);
        btnLoadDecryptKey.setPreferredSize(new Dimension(64, 44));
        btnLoadDecryptKey.setBackground(Color.decode("#D9D9D9"));
        btnLoadDecryptKey.setToolTipText("Tải key lên từ file .dat");
        pnlKeyTool.add(btnLoadDecryptKey);

        // thêm vào panel
        pnlKey.add(pnlKeyTool);
        panel.add(pnlKey);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        switch (cmd) {
            case "genKey": {
                int keySize = Integer.parseInt(cbbKeySize.getSelectedItem().toString());
                try {
                    KeyPair keyPair = cipher.genKey(keySize);
                    String publicKey = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
                    String privateKey = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
                    txtAreaPublicKey.setText(publicKey);
                    txtAreaPrivateKey.setText(privateKey);
                } catch (Exception ex) {
                    showErrorDialog(ex.getMessage());
                }
                break;
            }
            case "copyPublicKey": {
                String text = txtAreaPublicKey.getText();
                StringSelection selection = new StringSelection(text);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(selection, null);
                // kiểm tra nội dung trong clipboard
                Transferable transferData = clipboard.getContents(null);
                if(transferData != null && transferData.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                    try {
                        String copiedText = (String) transferData.getTransferData(DataFlavor.stringFlavor);
                        if(copiedText.equals(text)) {
                            btnCopyPublicKey.setBackground(Color.decode("#EDEBB9"));
                            Timer timer = new Timer(1000, new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    btnCopyPublicKey.setBackground(Color.decode("#D9D9D9"));
                                }
                            });
                            timer.setRepeats(false);
                            timer.start();
                        } else {
                            btnCopyPublicKey.setBackground(Color.decode("#D9D9D9"));
                        }
                    } catch (Exception ex) {
                        btnCopyPublicKey.setBackground(Color.decode("#D9D9D9"));
                    }
                }
                break;
            }
            case "copyPrivateKey": {
                String text = txtAreaPrivateKey.getText();
                StringSelection selection = new StringSelection(text);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(selection, null);
                // kiểm tra nội dung trong clipboard
                Transferable transferData = clipboard.getContents(null);
                if(transferData != null && transferData.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                    try {
                        String copiedText = (String) transferData.getTransferData(DataFlavor.stringFlavor);
                        if(copiedText.equals(text)) {
                            btnCopyPrivateKey.setBackground(Color.decode("#EDEBB9"));
                            Timer timer = new Timer(1000, new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    btnCopyPrivateKey.setBackground(Color.decode("#D9D9D9"));
                                }
                            });
                            timer.setRepeats(false);
                            timer.start();
                        } else {
                            btnCopyPrivateKey.setBackground(Color.decode("#D9D9D9"));
                        }
                    } catch (Exception ex) {
                        btnCopyPrivateKey.setBackground(Color.decode("#D9D9D9"));
                    }
                }
                break;
            }
            case "savePublicKey": {
                String text = txtAreaPublicKey.getText();
                if(text.isEmpty()) {
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
                        pw.write(text);
                        pw.close();
                    } catch (FileNotFoundException ex) {
                        showErrorDialog("Không tìm thấy file đích");
                    } catch (UnsupportedEncodingException ex) {
                        showErrorDialog("Kiểu encode không được hỗ trợ");
                    }
                }
                break;
            }
            case "savePrivateKey": {
                String text = txtAreaPrivateKey.getText();
                if(text.isEmpty()) {
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
                        pw.write(text);
                        pw.close();
                    } catch (FileNotFoundException ex) {
                        showErrorDialog("Không tìm thấy file đích");
                    } catch (UnsupportedEncodingException ex) {
                        showErrorDialog("Kiểu encode không được hỗ trợ");
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