package view;

import model.asymmetric.AsymmetricCipher;
import utils.FontUtils;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class AsymmetricCipherView extends JPanel {
    private final AsymmetricCipher cipher;
    public AsymmetricCipherView(AsymmetricCipher cipher) {
        this.cipher = cipher;
        init();
    }

    private void init() {
        renderSelf();
        renderKeyTool();
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
        JComboBox cbbKeySize = new JComboBox(cipher.getSupportedKeySize().toArray(new Integer[0]));
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
        btnGenKey.setFont(FontUtils.createRobotoFont("medium", 16f));
        pnlGenKey.add(btnGenKey);
        pnlKeyTool.add(pnlGenKey);
        // public key area
        JPanel pnlPublicKey = new JPanel(new BorderLayout());
        pnlPublicKey.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        JTextArea txtAreaPublicKey = new JTextArea();   // text area
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
        JButton btnCopyPublicKey = new JButton("Sao chép");
        btnCopyPublicKey.setFont(FontUtils.createRobotoFont("medium", 16f));
        JButton btnSavePublicKey = new JButton("Lưu");
        btnSavePublicKey.setFont(FontUtils.createRobotoFont("medium", 16f));
        pnlPublicKeyTool.add(btnCopyPublicKey);
        pnlPublicKeyTool.add(btnSavePublicKey);
        pnlPublicKey.add(pnlPublicKeyTool, BorderLayout.SOUTH);
        pnlKeyTool.add(pnlPublicKey);   // add
        // private key area
        JPanel pnlPrivateKey = new JPanel(new BorderLayout());
        pnlPrivateKey.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        JTextArea txtAreaPrivateKey = new JTextArea();   // text area
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
        JButton btnCopyPrivateKey = new JButton("Sao chép");
        btnCopyPrivateKey.setFont(FontUtils.createRobotoFont("medium", 16f));
        JButton btnSavePrivateKey = new JButton("Lưu");
        btnSavePrivateKey.setFont(FontUtils.createRobotoFont("medium", 16f));
        pnlPrivateKeyTool.add(btnCopyPrivateKey);
        pnlPrivateKeyTool.add(btnSavePrivateKey);
        pnlPrivateKey.add(pnlPrivateKeyTool, BorderLayout.SOUTH);
        pnlKeyTool.add(pnlPrivateKey);   // add
        this.add(pnlKeyTool, BorderLayout.WEST);
    }
}