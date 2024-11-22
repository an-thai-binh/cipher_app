package view.signature;

import model.signature.DigitalSignature;
import utils.FontUtils;
import utils.IconUtils;
import view.hash.HashAlgorithmView;

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
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class DigitalSignatureView extends JPanel implements ActionListener {
    private final DigitalSignature ds;
    private JComboBox cbbKeySize, cbbCipherName, cbbHashName;
    private JTextArea txtAreaPublicKey, txtAreaPrivateKey;
    private JButton btnCopyPublicKey, btnCopyPrivateKey;
    public DigitalSignatureView(DigitalSignature ds) {
        this.ds = ds;
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
        pnlKeySize.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        JLabel lblKeySize = new JLabel("Kích thước", JLabel.CENTER);
        lblKeySize.setFont(FontUtils.createRobotoFont("medium", 16f));
        cbbKeySize = new JComboBox(DigitalSignature.KEY_SIZES);
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
        pnlGenKey.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        JButton btnGenKey = new JButton("Tạo bộ key");
        btnGenKey.setActionCommand("genKey");
        btnGenKey.addActionListener(this);
        btnGenKey.setFont(FontUtils.createRobotoFont("medium", 16f));
        pnlGenKey.add(btnGenKey);
        pnlKeyTool.add(pnlGenKey);
        // public key area
        JLabel lblPublicKey = new JLabel("Public key", JLabel.CENTER);
        lblPublicKey.setFont(FontUtils.createRobotoFont("medium", 16f));
        lblPublicKey.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlKeyTool.add(lblPublicKey);
        JPanel pnlPublicKey = new JPanel(new BorderLayout());
        pnlPublicKey.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        txtAreaPublicKey = new JTextArea();   // text area
        txtAreaPublicKey.setLineWrap(true);
        txtAreaPublicKey.setWrapStyleWord(true);
        txtAreaPublicKey.setFont(FontUtils.createRobotoFont("regular", 16f));
        txtAreaPublicKey.setForeground(Color.black);
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
        JLabel lblPrivateKey = new JLabel("Private key", JLabel.CENTER);
        lblPrivateKey.setFont(FontUtils.createRobotoFont("medium", 16f));
        lblPrivateKey.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlKeyTool.add(lblPrivateKey);
        JPanel pnlPrivateKey = new JPanel(new BorderLayout());
        pnlPrivateKey.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        txtAreaPrivateKey = new JTextArea();   // text area
        txtAreaPrivateKey.setLineWrap(true);
        txtAreaPrivateKey.setWrapStyleWord(true);
        txtAreaPrivateKey.setFont(FontUtils.createRobotoFont("regular", 16f));
        txtAreaPrivateKey.setForeground(Color.black);
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
        JLabel lblCipherTitle = new JLabel(new String("Chữ ký điện tử").toUpperCase(), JLabel.CENTER);
        lblCipherTitle.setFont(FontUtils.createRobotoFont("extraBold", 32f));
        lblCipherTitle.setForeground(Color.BLACK);
        lblCipherTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        lblCipherTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlMain.add(lblCipherTitle);
        // name
        JPanel pnlName = new JPanel(new GridLayout(1, 2));
        pnlName.setBackground(null);
        // cipher name
        JPanel pnlCipherName = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlCipherName.setBackground(null);
        JLabel lblCipherName = new JLabel("Giải thuật ký:", JLabel.CENTER);
        lblCipherName.setFont(FontUtils.createRobotoFont("medium", 16f));
        pnlCipherName.add(lblCipherName);
        cbbCipherName = new JComboBox(DigitalSignature.ALGORITHMS);
        cbbCipherName.setBackground(Color.WHITE);
        cbbCipherName.setPreferredSize(new Dimension(150, 30));
        cbbCipherName.setRenderer(new DefaultListCellRenderer() {
            @Override
            public void paint(Graphics g) {
                setFont(FontUtils.createRobotoFont("medium", 20f));
                setHorizontalAlignment(DefaultListCellRenderer.CENTER); // center item
                super.paint(g);
            }
        });
        pnlCipherName.add(cbbCipherName);
        pnlName.add(pnlCipherName);
        // hash name
        JPanel pnlHashName = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlHashName.setBackground(null);
        JLabel lblHashName = new JLabel("Giải thuật hash:", JLabel.CENTER);
        lblHashName.setFont(FontUtils.createRobotoFont("medium", 16f));
        pnlHashName.add(lblHashName);
        cbbHashName = new JComboBox(DigitalSignature.HASH_ALGORITHMS);
        cbbHashName.setBackground(Color.WHITE);
        cbbHashName.setPreferredSize(new Dimension(150, 30));
        cbbHashName.setRenderer(new DefaultListCellRenderer() {
            @Override
            public void paint(Graphics g) {
                setFont(FontUtils.createRobotoFont("medium", 20f));
                setHorizontalAlignment(DefaultListCellRenderer.CENTER); // center item
                super.paint(g);
            }
        });
        pnlHashName.add(cbbHashName);
        pnlName.add(pnlHashName);
        pnlMain.add(pnlName);
        // action section
        JTabbedPane tabPaneAction = new JTabbedPane();
        tabPaneAction.setFont(FontUtils.createRobotoFont("regular", 16f));
        DigitalSignatureTypeView textPanel = new DigitalSignatureTypeView(true);
        DigitalSignatureTypeView filePanel = new DigitalSignatureTypeView(false);
        tabPaneAction.addTab("Văn bản", textPanel);
        tabPaneAction.addTab("File", filePanel);
        pnlMain.add(tabPaneAction);
        this.add(pnlMain, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        switch (cmd) {
            case "genKey": {
                try {
                    ds.setAsymmetricCipher(cbbCipherName.getSelectedItem().toString());
                    KeyPair keyPair = ds.genKey(Integer.parseInt(cbbKeySize.getSelectedItem().toString()));
                    String pubKeyBase64 = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
                    String priKeyBase64 = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
                    txtAreaPublicKey.setText(pubKeyBase64);
                    txtAreaPrivateKey.setText(priKeyBase64);
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
                                    btnCopyPublicKey.setBackground(UIManager.getColor("Button.background"));
                                }
                            });
                            timer.setRepeats(false);
                            timer.start();
                        }
                    } catch (Exception ex) {
                        showErrorDialog(ex.getMessage());
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
                                    btnCopyPrivateKey.setBackground(UIManager.getColor("Button.background"));
                                }
                            });
                            timer.setRepeats(false);
                            timer.start();
                        }
                    } catch (Exception ex) {
                        showErrorDialog(ex.getMessage());
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

    class DigitalSignatureTypeView extends JPanel implements ActionListener {
        private boolean isTextPanel;
        private JTextArea txtAreaInputSign, txtAreaOutputSign, txtAreaSignKey, txtAreaInputVerify, txtAreaVerifyKey, txtAreaSignValue;
        private JTextField txtFieldInputSign, txtFieldInputVerify;
        private JButton btnLoadSignKey, btnLoadVerifyKey, btnCopySign, btnLoadSignValue, btnOutput;
        public DigitalSignatureTypeView(boolean isTextPanel) {
            this.isTextPanel = isTextPanel;
            init();
        }

        private void init() {
            renderSelf();
            if(isTextPanel) {
                renderSignSide();
                renderVerifySide();
            } else {
                renderSignFileSide();
                renderVerifyFileSide();
            }
        }

        private void renderSelf(){
            this.setLayout(new GridLayout(1, 2));
            this.setBackground(Color.decode("#F4F6FF"));
        }

        private void renderSignSide() {
            JPanel pnlSign = new JPanel();
            pnlSign.setBackground(null);
            pnlSign.setLayout(new BoxLayout(pnlSign, BoxLayout.Y_AXIS));
            pnlSign.setBorder(BorderFactory.createEmptyBorder(0, 10, 20, 10));
            // title
            JLabel lblSignTitle = new JLabel("Ký dữ liệu", JLabel.CENTER);
            lblSignTitle.setFont(FontUtils.createRobotoFont("medium", 16));
            lblSignTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
            lblSignTitle.setBorder(BorderFactory.createEmptyBorder(5,0, 5, 0));
            pnlSign.add(lblSignTitle);
            // input
            txtAreaInputSign = new JTextArea();   // text area
            txtAreaInputSign.setLineWrap(true);
            txtAreaInputSign.setWrapStyleWord(true);
            txtAreaInputSign.setFont(FontUtils.createRobotoFont("regular", 16f));
            txtAreaInputSign.setForeground(Color.black);
            TitledBorder borderInput = BorderFactory.createTitledBorder("Nhập văn bản cần ký");
            borderInput.setTitleFont(FontUtils.createRobotoFont("regular", 13f));
            txtAreaInputSign.setBorder(borderInput);
            JScrollPane inputSignScrollPane = new JScrollPane(txtAreaInputSign);
            inputSignScrollPane.setPreferredSize(new Dimension(0, 200));
            inputSignScrollPane.setBackground(null);
            pnlSign.add(inputSignScrollPane);
            // key label
            JLabel lblPrivateKey = new JLabel("Nhập private key:", JLabel.CENTER);
            lblPrivateKey.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
            lblPrivateKey.setFont(FontUtils.createRobotoFont("medium", 16f));
            lblPrivateKey.setAlignmentX(Component.CENTER_ALIGNMENT);
            pnlSign.add(lblPrivateKey);
            // key
            renderSignKeyRow(pnlSign);
            // action button
            JPanel pnlSignAction = new JPanel(new FlowLayout(FlowLayout.CENTER));
            pnlSignAction.setBackground(null);
            JButton btnSign = new JButton("Ký");
            btnSign.setActionCommand("sign");
            btnSign.addActionListener(this);
            btnSign.setPreferredSize(new Dimension(175, 40));
            btnSign.setBackground(Color.decode("#F3C623"));
            btnSign.setFont(FontUtils.createRobotoFont("medium", 24f));
            pnlSignAction.add(btnSign);
            pnlSign.add(pnlSignAction);
            // output
            JPanel pnlOutput = new JPanel(new BorderLayout());
            pnlOutput.setBackground(null);
            txtAreaOutputSign = new JTextArea();   // text area
            txtAreaOutputSign.setEditable(false);
            txtAreaOutputSign.setLineWrap(true);
            txtAreaOutputSign.setWrapStyleWord(true);
            txtAreaOutputSign.setFont(FontUtils.createRobotoFont("regular", 16f));
            txtAreaOutputSign.setForeground(Color.black);
            TitledBorder borderOutput = BorderFactory.createTitledBorder("Chữ ký số");
            borderOutput.setTitleFont(FontUtils.createRobotoFont("regular", 13f));
            txtAreaOutputSign.setBorder(borderOutput);
            JScrollPane outputSignScrollPane = new JScrollPane(txtAreaOutputSign);
            outputSignScrollPane.setPreferredSize(new Dimension(0, 200));
            outputSignScrollPane.setBackground(null);
            pnlOutput.add(outputSignScrollPane, BorderLayout.CENTER);
            JPanel pnlOutputTool = new JPanel(new FlowLayout(FlowLayout.CENTER));
            pnlOutputTool.setBackground(null);
            btnCopySign = new JButton("Sao chép");
            btnCopySign.setActionCommand("copySign");
            btnCopySign.addActionListener(this);
            btnCopySign.setFont(FontUtils.createRobotoFont("medium", 16f));
            JButton btnSaveSign = new JButton("Lưu");
            btnSaveSign.setActionCommand("saveSign");
            btnSaveSign.addActionListener(this);
            btnSaveSign.setFont(FontUtils.createRobotoFont("medium", 16f));
            pnlOutputTool.add(btnCopySign);
            pnlOutputTool.add(btnSaveSign);
            pnlOutput.add(pnlOutputTool, BorderLayout.SOUTH);
            pnlSign.add(pnlOutput);
            this.add(pnlSign);
        }

        private void renderSignFileSide() {
            JPanel pnlSign = new JPanel();
            pnlSign.setBackground(null);
            pnlSign.setLayout(new BoxLayout(pnlSign, BoxLayout.Y_AXIS));
            pnlSign.setBorder(BorderFactory.createEmptyBorder(0, 10, 20, 10));
            // title
            JLabel lblSignTitle = new JLabel("Ký dữ liệu", JLabel.CENTER);
            lblSignTitle.setFont(FontUtils.createRobotoFont("medium", 16));
            lblSignTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
            lblSignTitle.setBorder(BorderFactory.createEmptyBorder(5,0, 5, 0));
            pnlSign.add(lblSignTitle);
            // input
            JPanel pnlInputFile = new JPanel(new BorderLayout());
            pnlInputFile.setBackground(null);
            pnlInputFile.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
            // button input file
            JButton btnInputFile = new JButton("CHỌN FILE CẦN KÝ");
            btnInputFile.setVerticalTextPosition(SwingConstants.TOP);
            btnInputFile.setHorizontalTextPosition(SwingConstants.CENTER);
            btnInputFile.setBackground(Color.WHITE);
            btnInputFile.setFont(FontUtils.createRobotoFont("medium", 20f));
            btnInputFile.setIcon(IconUtils.UPLOAD_ICON);
            btnInputFile.setActionCommand("chooseSignFile");
            btnInputFile.addActionListener(this);
            btnInputFile.setDropTarget(new DropTarget() {
                @Override
                public synchronized void drop(DropTargetDropEvent dtde) {
                    try {
                        dtde.acceptDrop(DnDConstants.ACTION_COPY);
                        java.util.List<File> droppedFiles = (List<File>) dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                        if(droppedFiles.size() == 1) {
                            File file = droppedFiles.get(0);
                            if(!file.isDirectory()) {
                                txtFieldInputSign.setText(file.getAbsolutePath());
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
            JPanel pnlChooseInput = new JPanel(new GridLayout(1, 1));
            pnlChooseInput.setBackground(null);
            pnlChooseInput.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
            JPanel pnlInputPath = new JPanel();
            pnlInputPath.setLayout(new BoxLayout(pnlInputPath, BoxLayout.X_AXIS));
            pnlInputPath.setBackground(null);
            JLabel lblInputPath = new JLabel("Đường dẫn file nguồn:");
            lblInputPath.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
            lblInputPath.setFont(FontUtils.createRobotoFont("medium", 16f));
            txtFieldInputSign = new JTextField();
            txtFieldInputSign.setFont(FontUtils.createRobotoFont("regular", 16f));
            pnlInputPath.add(lblInputPath);
            pnlInputPath.add(txtFieldInputSign);
            pnlChooseInput.add(pnlInputPath);
            pnlInputFile.add(pnlChooseInput, BorderLayout.SOUTH);
            pnlSign.add(pnlInputFile);
            // key label
            JLabel lblPrivateKey = new JLabel("Nhập private key:", JLabel.CENTER);
            lblPrivateKey.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
            lblPrivateKey.setFont(FontUtils.createRobotoFont("medium", 16f));
            lblPrivateKey.setAlignmentX(Component.CENTER_ALIGNMENT);
            pnlSign.add(lblPrivateKey);
            // key
            renderSignKeyRow(pnlSign);
            // action button
            JPanel pnlSignAction = new JPanel(new FlowLayout(FlowLayout.CENTER));
            pnlSignAction.setBackground(null);
            JButton btnSign = new JButton("Ký");
            btnSign.setActionCommand("signFile");
            btnSign.addActionListener(this);
            btnSign.setPreferredSize(new Dimension(175, 40));
            btnSign.setBackground(Color.decode("#F3C623"));
            btnSign.setFont(FontUtils.createRobotoFont("medium", 24f));
            pnlSignAction.add(btnSign);
            pnlSign.add(pnlSignAction);
            // output
            JPanel pnlOutput = new JPanel(new BorderLayout());
            pnlOutput.setBackground(null);
            txtAreaOutputSign = new JTextArea();   // text area
            txtAreaOutputSign.setEditable(false);
            txtAreaOutputSign.setLineWrap(true);
            txtAreaOutputSign.setWrapStyleWord(true);
            txtAreaOutputSign.setFont(FontUtils.createRobotoFont("regular", 16f));
            txtAreaOutputSign.setForeground(Color.black);
            TitledBorder borderOutput = BorderFactory.createTitledBorder("Chữ ký số");
            borderOutput.setTitleFont(FontUtils.createRobotoFont("regular", 13f));
            txtAreaOutputSign.setBorder(borderOutput);
            JScrollPane outputSignScrollPane = new JScrollPane(txtAreaOutputSign);
            outputSignScrollPane.setPreferredSize(new Dimension(0, 140));
            outputSignScrollPane.setBackground(null);
            pnlOutput.add(outputSignScrollPane, BorderLayout.CENTER);
            JPanel pnlOutputTool = new JPanel(new FlowLayout(FlowLayout.CENTER));
            pnlOutputTool.setBackground(null);
            btnCopySign = new JButton("Sao chép");
            btnCopySign.setActionCommand("copySign");
            btnCopySign.addActionListener(this);
            btnCopySign.setFont(FontUtils.createRobotoFont("medium", 16f));
            JButton btnSaveSign = new JButton("Lưu");
            btnSaveSign.setActionCommand("saveSign");
            btnSaveSign.addActionListener(this);
            btnSaveSign.setFont(FontUtils.createRobotoFont("medium", 16f));
            pnlOutputTool.add(btnCopySign);
            pnlOutputTool.add(btnSaveSign);
            pnlOutput.add(pnlOutputTool, BorderLayout.SOUTH);
            pnlSign.add(pnlOutput);
            this.add(pnlSign);
        }

        private void renderVerifySide() {
            JPanel pnlVerify = new JPanel();
            pnlVerify.setBackground(null);
            pnlVerify.setLayout(new BoxLayout(pnlVerify, BoxLayout.Y_AXIS));
            pnlVerify.setBorder(BorderFactory.createEmptyBorder(0, 10, 20, 10));
            // title
            JLabel lblVerifyTitle = new JLabel("Xác minh", JLabel.CENTER);
            lblVerifyTitle.setFont(FontUtils.createRobotoFont("medium", 16));
            lblVerifyTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
            lblVerifyTitle.setBorder(BorderFactory.createEmptyBorder(5,0, 5, 0));
            pnlVerify.add(lblVerifyTitle);
            // input
            txtAreaInputVerify = new JTextArea();   // text area
            txtAreaInputVerify.setLineWrap(true);
            txtAreaInputVerify.setWrapStyleWord(true);
            txtAreaInputVerify.setFont(FontUtils.createRobotoFont("regular", 16f));
            txtAreaInputVerify.setForeground(Color.black);
            TitledBorder borderInput = BorderFactory.createTitledBorder("Nhập văn bản cần xác minh");
            borderInput.setTitleFont(FontUtils.createRobotoFont("regular", 13f));
            txtAreaInputVerify.setBorder(borderInput);
            JScrollPane inputVerifyScrollPane = new JScrollPane(txtAreaInputVerify);
            inputVerifyScrollPane.setPreferredSize(new Dimension(0, 200));
            inputVerifyScrollPane.setBackground(null);
            pnlVerify.add(inputVerifyScrollPane);
            // sign and key input panel
            JPanel pnlSignKey = new JPanel(new GridLayout(2, 1));
            pnlSignKey.setBackground(null);
            // sign panel
            JPanel pnlSignValue = new JPanel(new BorderLayout());
            pnlSignValue.setBackground(null);
            JLabel lblSignValue = new JLabel("Nhập chữ ký:", JLabel.CENTER);
            lblSignValue.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
            lblSignValue.setFont(FontUtils.createRobotoFont("medium", 16f));
            lblSignValue.setAlignmentX(Component.CENTER_ALIGNMENT);
            pnlSignValue.add(lblSignValue, BorderLayout.NORTH);
            renderSignValueRow(pnlSignValue);
            pnlSignKey.add(pnlSignValue);
            // key panel
            JPanel pnlKeyValue = new JPanel(new BorderLayout());
            pnlKeyValue.setBackground(null);
            JLabel lblPublicKey = new JLabel("Nhập public key:", JLabel.CENTER);
            lblPublicKey.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
            lblPublicKey.setFont(FontUtils.createRobotoFont("medium", 16f));
            lblPublicKey.setAlignmentX(Component.CENTER_ALIGNMENT);
            pnlKeyValue.add(lblPublicKey, BorderLayout.NORTH);
            renderVerifyKeyRow(pnlKeyValue);
            pnlSignKey.add(pnlKeyValue);
            // add sign and key input panel
            pnlVerify.add(pnlSignKey);
            // action button
            JPanel pnlVerifyAction = new JPanel(new FlowLayout(FlowLayout.CENTER));
            pnlVerifyAction.setBackground(null);
            pnlVerifyAction.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            JButton btnVerify = new JButton("Xác minh");
            btnVerify.setActionCommand("verify");
            btnVerify.addActionListener(this);
            btnVerify.setPreferredSize(new Dimension(175, 40));
            btnVerify.setBackground(Color.decode("#F3C623"));
            btnVerify.setFont(FontUtils.createRobotoFont("medium", 24f));
            pnlVerifyAction.add(btnVerify);
            pnlVerify.add(pnlVerifyAction);
            // output
            JPanel pnlOutput = new JPanel(new GridLayout(1, 1));
            pnlOutput.setPreferredSize(new Dimension(0, 145));
            btnOutput = new JButton();
            btnOutput.setFont(FontUtils.createRobotoFont("medium", 20f));
            btnOutput.setOpaque(true);
            btnOutput.setEnabled(false);
            btnOutput.setPreferredSize(new Dimension(0, 60));
            pnlOutput.add(btnOutput);
            pnlVerify.add(pnlOutput);
            this.add(pnlVerify);
        }

        private void renderVerifyFileSide() {
            JPanel pnlVerify = new JPanel();
            pnlVerify.setBackground(null);
            pnlVerify.setLayout(new BoxLayout(pnlVerify, BoxLayout.Y_AXIS));
            pnlVerify.setBorder(BorderFactory.createEmptyBorder(0, 10, 20, 10));
            // title
            JLabel lblVerifyTitle = new JLabel("Xác minh", JLabel.CENTER);
            lblVerifyTitle.setFont(FontUtils.createRobotoFont("medium", 16));
            lblVerifyTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
            lblVerifyTitle.setBorder(BorderFactory.createEmptyBorder(5,0, 5, 0));
            pnlVerify.add(lblVerifyTitle);
            // input
            JPanel pnlInputFile = new JPanel(new BorderLayout());
            pnlInputFile.setBackground(null);
            pnlInputFile.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
            // button input file
            JButton btnInputFile = new JButton("CHỌN FILE CẦN XÁC MINH");
            btnInputFile.setVerticalTextPosition(SwingConstants.TOP);
            btnInputFile.setHorizontalTextPosition(SwingConstants.CENTER);
            btnInputFile.setBackground(Color.WHITE);
            btnInputFile.setFont(FontUtils.createRobotoFont("medium", 20f));
            btnInputFile.setIcon(IconUtils.UPLOAD_ICON);
            btnInputFile.setActionCommand("chooseVerifyFile");
            btnInputFile.addActionListener(this);
            btnInputFile.setDropTarget(new DropTarget() {
                @Override
                public synchronized void drop(DropTargetDropEvent dtde) {
                    try {
                        dtde.acceptDrop(DnDConstants.ACTION_COPY);
                        java.util.List<File> droppedFiles = (List<File>) dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                        if(droppedFiles.size() == 1) {
                            File file = droppedFiles.get(0);
                            if(!file.isDirectory()) {
                                txtFieldInputVerify.setText(file.getAbsolutePath());
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
            JPanel pnlChooseInput = new JPanel(new GridLayout(1, 1));
            pnlChooseInput.setBackground(null);
            pnlChooseInput.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
            JPanel pnlInputPath = new JPanel();
            pnlInputPath.setLayout(new BoxLayout(pnlInputPath, BoxLayout.X_AXIS));
            pnlInputPath.setBackground(null);
            JLabel lblInputPath = new JLabel("Đường dẫn file nguồn:");
            lblInputPath.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
            lblInputPath.setFont(FontUtils.createRobotoFont("medium", 16f));
            txtFieldInputVerify = new JTextField();
            txtFieldInputVerify.setFont(FontUtils.createRobotoFont("regular", 16f));
            pnlInputPath.add(lblInputPath);
            pnlInputPath.add(txtFieldInputVerify);
            pnlChooseInput.add(pnlInputPath);
            pnlInputFile.add(pnlChooseInput, BorderLayout.SOUTH);
            pnlVerify.add(pnlInputFile);
            // sign and key input panel
            JPanel pnlSignKey = new JPanel(new GridLayout(2, 1));
            pnlSignKey.setBackground(null);
            // sign panel
            JPanel pnlSignValue = new JPanel(new BorderLayout());
            pnlSignValue.setBackground(null);
            JLabel lblSignValue = new JLabel("Nhập chữ ký:", JLabel.CENTER);
            lblSignValue.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
            lblSignValue.setFont(FontUtils.createRobotoFont("medium", 16f));
            lblSignValue.setAlignmentX(Component.CENTER_ALIGNMENT);
            pnlSignValue.add(lblSignValue, BorderLayout.NORTH);
            renderSignValueRow(pnlSignValue);
            pnlSignKey.add(pnlSignValue);
            // key panel
            JPanel pnlKeyValue = new JPanel(new BorderLayout());
            pnlKeyValue.setBackground(null);
            JLabel lblPublicKey = new JLabel("Nhập public key:", JLabel.CENTER);
            lblPublicKey.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
            lblPublicKey.setFont(FontUtils.createRobotoFont("medium", 16f));
            lblPublicKey.setAlignmentX(Component.CENTER_ALIGNMENT);
            pnlKeyValue.add(lblPublicKey, BorderLayout.NORTH);
            renderVerifyKeyRow(pnlKeyValue);
            pnlSignKey.add(pnlKeyValue);
            // add sign and key input panel
            pnlVerify.add(pnlSignKey);
            // action button
            JPanel pnlVerifyAction = new JPanel(new FlowLayout(FlowLayout.CENTER));
            pnlVerifyAction.setBackground(null);
            pnlVerifyAction.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            JButton btnVerify = new JButton("Xác minh");
            btnVerify.setActionCommand("verifyFile");
            btnVerify.addActionListener(this);
            btnVerify.setPreferredSize(new Dimension(175, 40));
            btnVerify.setBackground(Color.decode("#F3C623"));
            btnVerify.setFont(FontUtils.createRobotoFont("medium", 24f));
            pnlVerifyAction.add(btnVerify);
            pnlVerify.add(pnlVerifyAction);
            // output
            JPanel pnlOutput = new JPanel(new GridLayout(1, 1));
            pnlOutput.setPreferredSize(new Dimension(0, 120));
            btnOutput = new JButton();
            btnOutput.setFont(FontUtils.createRobotoFont("medium", 20f));
            btnOutput.setOpaque(true);
            btnOutput.setEnabled(false);
            btnOutput.setPreferredSize(new Dimension(0, 60));
            pnlOutput.add(btnOutput);
            pnlVerify.add(pnlOutput);
            this.add(pnlVerify);
        }

        private void renderSignKeyRow(JPanel panel) {
            JPanel pnlKey = new JPanel();
            pnlKey.setLayout(new BoxLayout(pnlKey, BoxLayout.X_AXIS));
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
            txtAreaSignKey = new JTextArea();
            txtAreaSignKey.setFont(FontUtils.createRobotoFont("regular", 16f));
            txtAreaSignKey.setLineWrap(true);
            txtAreaSignKey.setWrapStyleWord(true);
            JScrollPane keyScrollPane = new JScrollPane(txtAreaSignKey);
            keyScrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            keyScrollPane.setPreferredSize(new Dimension(402, 74));
            pnlKey.add(keyScrollPane);

            // key tool
            JPanel pnlKeyTool = new JPanel(new GridLayout(1, 1));
            pnlKeyTool.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            btnLoadSignKey = new JButton(IconUtils.LOAD_ICON);
            btnLoadSignKey.setActionCommand("loadSignKey");
            btnLoadSignKey.addActionListener(this);
            btnLoadSignKey.setPreferredSize(new Dimension(64, 44));
            btnLoadSignKey.setBackground(Color.decode("#D9D9D9"));
            btnLoadSignKey.setToolTipText("Tải key lên từ file .dat");
            pnlKeyTool.add(btnLoadSignKey);

            // thêm vào panel
            pnlKey.add(pnlKeyTool);
            panel.add(pnlKey);
        }

        private void renderVerifyKeyRow(JPanel panel) {
            JPanel pnlKey = new JPanel();
            pnlKey.setLayout(new BoxLayout(pnlKey, BoxLayout.X_AXIS));
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
            txtAreaVerifyKey = new JTextArea();
            txtAreaVerifyKey.setFont(FontUtils.createRobotoFont("regular", 16f));
            txtAreaVerifyKey.setLineWrap(true);
            txtAreaVerifyKey.setWrapStyleWord(true);
            JScrollPane keyScrollPane = new JScrollPane(txtAreaVerifyKey);
            keyScrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            keyScrollPane.setPreferredSize(new Dimension(402, 74));
            pnlKey.add(keyScrollPane);

            // key tool
            JPanel pnlKeyTool = new JPanel(new GridLayout(1, 1));
            pnlKeyTool.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            btnLoadVerifyKey = new JButton(IconUtils.LOAD_ICON);
            btnLoadVerifyKey.setActionCommand("loadVerifyKey");
            btnLoadVerifyKey.addActionListener(this);
            btnLoadVerifyKey.setPreferredSize(new Dimension(64, 44));
            btnLoadVerifyKey.setBackground(Color.decode("#D9D9D9"));
            btnLoadVerifyKey.setToolTipText("Tải key lên từ file .dat");
            pnlKeyTool.add(btnLoadVerifyKey);

            // thêm vào panel
            pnlKey.add(pnlKeyTool);
            panel.add(pnlKey, BorderLayout.CENTER);
        }

        private void renderSignValueRow(JPanel panel) {
            JPanel pnlSignValue = new JPanel();
            pnlSignValue.setLayout(new BoxLayout(pnlSignValue, BoxLayout.X_AXIS));
            pnlSignValue.setBackground(null);

            // sign icon
            JPanel pnlSignIcon = new JPanel(new GridLayout(1, 1));
            pnlSignIcon.setPreferredSize(new Dimension(74, 74));
            pnlSignIcon.setBackground(Color.decode("#EB8317"));
            pnlSignIcon.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            JLabel lblSignIcon = new JLabel(IconUtils.SIGN_ICON, JLabel.CENTER);
            pnlSignIcon.add(lblSignIcon);
            pnlSignValue.add(pnlSignIcon);

            // sign value text area
            txtAreaSignValue = new JTextArea();
            txtAreaSignValue.setFont(FontUtils.createRobotoFont("regular", 16f));
            txtAreaSignValue.setLineWrap(true);
            txtAreaSignValue.setWrapStyleWord(true);
            JScrollPane signScrollPane = new JScrollPane(txtAreaSignValue);
            signScrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            signScrollPane.setPreferredSize(new Dimension(402, 74));
            pnlSignValue.add(signScrollPane);

            // sign tool
            JPanel pnlSignTool = new JPanel(new GridLayout(1, 1));
            pnlSignTool.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            btnLoadSignValue = new JButton(IconUtils.LOAD_ICON);
            btnLoadSignValue.setActionCommand("loadSign");
            btnLoadSignValue.addActionListener(this);
            btnLoadSignValue.setPreferredSize(new Dimension(64, 44));
            btnLoadSignValue.setBackground(Color.decode("#D9D9D9"));
            btnLoadSignValue.setToolTipText("Tải key lên từ file .dat");
            pnlSignTool.add(btnLoadSignValue);

            // thêm vào panel
            pnlSignValue.add(pnlSignTool);
            panel.add(pnlSignValue);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String cmd = e.getActionCommand();
            switch (cmd) {
                case "loadSignKey": {
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
                            txtAreaSignKey.setText("");
                            String line;
                            ArrayList<String> lineList = new ArrayList<>();
                            while((line = br.readLine()) != null) {
                                lineList.add(line);
                            }
                            for(int i = 0; i < lineList.size(); i++) {
                                txtAreaSignKey.append(lineList.get(i));
                                if(i < lineList.size() - 1) txtAreaSignKey.append("\n");    // chỉ thêm \n ở dòng cuối cùng
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
                case "copySign": {
                    String text = txtAreaOutputSign.getText();
                    StringSelection selection = new StringSelection(text);
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    clipboard.setContents(selection, null);
                    // kiểm tra nội dung trong clipboard
                    Transferable transferData = clipboard.getContents(null);
                    if(transferData != null && transferData.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                        try {
                            String copiedText = (String) transferData.getTransferData(DataFlavor.stringFlavor);
                            if(copiedText.equals(text)) {
                                btnCopySign.setBackground(Color.decode("#EDEBB9"));
                                Timer timer = new Timer(1000, new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        btnCopySign.setBackground(UIManager.getColor("Button.background"));
                                    }
                                });
                                timer.setRepeats(false);
                                timer.start();
                            }
                        } catch (Exception ex) {
                            showErrorDialog(ex.getMessage());
                        }
                    }
                    break;
                }
                case "saveSign": {
                    String text = txtAreaOutputSign.getText();
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
                case "loadSign": {
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
                            txtAreaSignValue.setText("");
                            String line;
                            ArrayList<String> lineList = new ArrayList<>();
                            while((line = br.readLine()) != null) {
                                lineList.add(line);
                            }
                            for(int i = 0; i < lineList.size(); i++) {
                                txtAreaSignValue.append(lineList.get(i));
                                if(i < lineList.size() - 1) txtAreaSignValue.append("\n");    // chỉ thêm \n ở dòng cuối cùng
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
                case "loadVerifyKey": {
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
                            txtAreaVerifyKey.setText("");
                            String line;
                            ArrayList<String> lineList = new ArrayList<>();
                            while((line = br.readLine()) != null) {
                                lineList.add(line);
                            }
                            for(int i = 0; i < lineList.size(); i++) {
                                txtAreaVerifyKey.append(lineList.get(i));
                                if(i < lineList.size() - 1) txtAreaVerifyKey.append("\n");    // chỉ thêm \n ở dòng cuối cùng
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
                case "chooseSignFile": {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setDialogTitle("Chọn file thao tác");
                    int userOption = fileChooser.showOpenDialog(DigitalSignatureView.this.getParent());
                    if(userOption == JFileChooser.APPROVE_OPTION) {
                        File inputFile = fileChooser.getSelectedFile();
                        txtFieldInputSign.setText(inputFile.getAbsolutePath());
                    }
                    break;
                }
                case "chooseVerifyFile": {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setDialogTitle("Chọn file thao tác");
                    int userOption = fileChooser.showOpenDialog(DigitalSignatureView.this.getParent());
                    if(userOption == JFileChooser.APPROVE_OPTION) {
                        File inputFile = fileChooser.getSelectedFile();
                        txtFieldInputVerify.setText(inputFile.getAbsolutePath());
                    }
                    break;
                }
                case "sign": {
                    // check input
                    String input = txtAreaInputSign.getText();
                    if(input.isEmpty()) {
                        showErrorDialog("Vui lòng nhập nội dung cần ký");
                        break;
                    }
                    // check key
                    String keyStr = txtAreaSignKey.getText();
                    if(keyStr.isEmpty()) {
                        showErrorDialog("Vui lòng nhập private key");
                        break;
                    }
                    try {
                        ds.setAsymmetricCipher(cbbCipherName.getSelectedItem().toString());
                        ds.setHashAlgorithm(cbbHashName.getSelectedItem().toString());
                        ds.setSignature();
                        byte[] key = Base64.getDecoder().decode(keyStr);
                        ds.loadPrivateKey(key);
                        txtAreaOutputSign.setText(ds.sign(input));
                    } catch (Exception ex) {
                        showErrorDialog(ex.getMessage());
                    }
                    break;
                }
                case "verify": {
                    // check input
                    String input = txtAreaInputVerify.getText();
                    if(input.isEmpty()) {
                        showErrorDialog("Vui lòng nhập nội dung cần ký");
                        break;
                    }
                    // check sign
                    String signBase64 = txtAreaSignValue.getText();
                    if(signBase64.isEmpty()) {
                        showErrorDialog("Vui lòng nhập chữ ký số");
                        break;
                    }
                    // check key
                    String keyStr = txtAreaVerifyKey.getText();
                    if(keyStr.isEmpty()) {
                        showErrorDialog("Vui lòng nhập private key");
                        break;
                    }
                    try {
                        ds.setAsymmetricCipher(cbbCipherName.getSelectedItem().toString());
                        ds.setHashAlgorithm(cbbHashName.getSelectedItem().toString());
                        ds.setSignature();
                        byte[] key = Base64.getDecoder().decode(keyStr);
                        ds.loadPublicKey(key);
                        boolean isValid = ds.verify(input, signBase64);
                        if(isValid) {
                            btnOutput.setBackground(Color.decode("#03962a"));
                            btnOutput.setForeground(Color.decode("#03962a"));
                            btnOutput.setText(new String("Thông tin hợp lệ").toUpperCase());
                        } else {
                            btnOutput.setBackground(Color.RED);
                            btnOutput.setForeground(Color.RED);
                            btnOutput.setText(new String("Thông tin không hợp lệ").toUpperCase());
                        }
                    } catch (Exception ex) {
                        showErrorDialog(ex.getMessage());
                    }
                    break;
                }
                case "signFile": {
                    // src
                    String src = txtFieldInputSign.getText();
                    // check key
                    String keyStr = txtAreaSignKey.getText();
                    if(keyStr.isEmpty()) {
                        showErrorDialog("Vui lòng nhập private key");
                        break;
                    }
                    try {
                        ds.setAsymmetricCipher(cbbCipherName.getSelectedItem().toString());
                        ds.setHashAlgorithm(cbbHashName.getSelectedItem().toString());
                        ds.setSignature();
                        byte[] key = Base64.getDecoder().decode(keyStr);
                        ds.loadPrivateKey(key);
                        txtAreaOutputSign.setText(ds.signFile(src));
                    } catch (Exception ex) {
                        showErrorDialog(ex.getMessage());
                    }
                    break;
                }
                case "verifyFile": {
                    // src
                    String src = txtFieldInputVerify.getText();
                    // check sign
                    String signBase64 = txtAreaSignValue.getText();
                    if(signBase64.isEmpty()) {
                        showErrorDialog("Vui lòng nhập chữ ký số");
                        break;
                    }
                    // check key
                    String keyStr = txtAreaVerifyKey.getText();
                    if(keyStr.isEmpty()) {
                        showErrorDialog("Vui lòng nhập private key");
                        break;
                    }
                    try {
                        ds.setAsymmetricCipher(cbbCipherName.getSelectedItem().toString());
                        ds.setHashAlgorithm(cbbHashName.getSelectedItem().toString());
                        ds.setSignature();
                        byte[] key = Base64.getDecoder().decode(keyStr);
                        ds.loadPublicKey(key);
                        boolean isValid = ds.verifyFile(src, signBase64);
                        if(isValid) {
                            btnOutput.setBackground(Color.decode("#03962a"));
                            btnOutput.setForeground(Color.decode("#03962a"));
                            btnOutput.setText(new String("Thông tin hợp lệ").toUpperCase());
                        } else {
                            btnOutput.setBackground(Color.RED);
                            btnOutput.setForeground(Color.RED);
                            btnOutput.setText(new String("Thông tin không hợp lệ").toUpperCase());
                        }
                    } catch (Exception ex) {
                        showErrorDialog(ex.getMessage());
                    }
                    break;
                }
            }
        }
    }
}
