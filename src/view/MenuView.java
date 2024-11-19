package view;

import utils.CipherSupport;
import utils.FontUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

public class MenuView extends JPanel {
    private final ActionListener listener;
    public MenuView(ActionListener listener) {
        this.listener = listener;
        init();
    }

    /**
     * init cài đặt giao diện cho trang chủ
     */
    private void init() {
        renderSelf();
        renderTitleRow();
        renderClassicalRow();
        renderSymmetricRow();
        renderAsymmetricRow();
        renderHashRow();
        renderDigitalSignatureRow();
    }

    /**
     * renderSelf   cài đặt giao diện chính
     */
    private void renderSelf() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
    }

    /**
     * renderTitleRow   cài đặt giao diện dòng tiêu đề
     */
    private void renderTitleRow() {
        // tiêu đề
        JLabel lblMainTitle = new JLabel("CÔNG CỤ MÃ HÓA VÀ GIẢI MÃ", JLabel.CENTER);
        lblMainTitle.setFont(FontUtils.createRobotoFont("extraBold", 32f));
        lblMainTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(lblMainTitle);
    }

    /**
     * renderClassicalRow  cài đặt giao diện dòng mã hóa cổ điển
     */
    private void renderClassicalRow() {
        JPanel pnlRow1 = new JPanel(new BorderLayout());
        pnlRow1.setPreferredSize(new Dimension(0, 70));
        pnlRow1.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        pnlRow1.setBackground(Color.WHITE);
        // JLabel
        JLabel lblRow1Type = new JLabel("MÃ HÓA CỔ ĐIỂN", JLabel.CENTER);
        lblRow1Type.setFont(FontUtils.createRobotoFont("medium", 24f));
        lblRow1Type.setForeground(Color.WHITE);
        lblRow1Type.setBackground(Color.decode("#10375C"));
        lblRow1Type.setOpaque(true);
        lblRow1Type.setPreferredSize(new Dimension(400, 0));
        pnlRow1.add(lblRow1Type, BorderLayout.WEST);
        // các JButton
        JPanel pnlRow1Btn = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        pnlRow1Btn.setBackground(Color.decode("#F4F6FF"));
        pnlRow1Btn.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        JButton btnClassical;
        for(String c: CipherSupport.CLASSICAL_CIPHERS) {
            btnClassical = new JButton(c);
            btnClassical.setBackground(Color.WHITE);
            btnClassical.setFont(FontUtils.createRobotoFont("regular", 20f));
            btnClassical.setPreferredSize(new Dimension(170, 30));
            btnClassical.addActionListener(this.listener);
            pnlRow1Btn.add(btnClassical);
        }
        pnlRow1.add(pnlRow1Btn, BorderLayout.CENTER);
        this.add(pnlRow1);
    }

    /**
     * renderSymmetricRow   cài đặt giao diện mã hóa đối xứng hiện đại
     */
    private void renderSymmetricRow() {
        JPanel pnlRow2 = new JPanel(new BorderLayout());
        pnlRow2.setPreferredSize(new Dimension(0, 70));
        pnlRow2.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        pnlRow2.setBackground(Color.WHITE);
        // JLabel
        JLabel lblRow2Type = new JLabel("MÃ HÓA ĐỐI XỨNG HIỆN ĐẠI", JLabel.CENTER);
        lblRow2Type.setFont(FontUtils.createRobotoFont("medium", 24f));
        lblRow2Type.setForeground(Color.WHITE);
        lblRow2Type.setBackground(Color.decode("#10375C"));
        lblRow2Type.setOpaque(true);
        lblRow2Type.setPreferredSize(new Dimension(400, 0));
        pnlRow2.add(lblRow2Type, BorderLayout.WEST);
        // các JButton
        JPanel pnlRow2Btn = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        pnlRow2Btn.setBackground(Color.decode("#F4F6FF"));
        JButton btnSymmetric;
        ArrayList<String> symmetricCipherList = new ArrayList<>();
        symmetricCipherList.addAll(Arrays.asList(CipherSupport.SYMMETRIC_CIPHERS));
        symmetricCipherList.addAll(Arrays.asList(CipherSupport.SYMMETRIC_CIPHERS_THIRD_PARTY));
        for(String c: symmetricCipherList) {
            btnSymmetric = new JButton(c);
            btnSymmetric.setBackground(Color.WHITE);
            btnSymmetric.setFont(FontUtils.createRobotoFont("regular", 20f));
            btnSymmetric.setPreferredSize(new Dimension(140, 30));
            btnSymmetric.addActionListener(this.listener);
            pnlRow2Btn.add(btnSymmetric);
        }
        pnlRow2.add(pnlRow2Btn, BorderLayout.CENTER);
        this.add(pnlRow2);
    }

    /**
     * renderAsymmetricRow   cài đặt giao diện mã hóa bất đối xứng
     */
    private void renderAsymmetricRow() {
        JPanel pnlRow3 = new JPanel(new BorderLayout());
        pnlRow3.setPreferredSize(new Dimension(0, 70));
        pnlRow3.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        pnlRow3.setBackground(Color.WHITE);
        // JLabel
        JLabel lblRow3Type = new JLabel("MÃ HÓA BẤT ĐỐI XỨNG", JLabel.CENTER);
        lblRow3Type.setFont(FontUtils.createRobotoFont("medium", 24f));
        lblRow3Type.setForeground(Color.WHITE);
        lblRow3Type.setBackground(Color.decode("#10375C"));
        lblRow3Type.setOpaque(true);
        lblRow3Type.setPreferredSize(new Dimension(400, 0));
        pnlRow3.add(lblRow3Type, BorderLayout.WEST);
        // các JButton
        JPanel pnlRow3Btn = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        pnlRow3Btn.setBackground(Color.decode("#F4F6FF"));
        JButton btnAsymmetric;
        for(String c: CipherSupport.ASYMMETRIC_CIPHERS) {
            btnAsymmetric = new JButton(c);
            btnAsymmetric.setBackground(Color.WHITE);
            btnAsymmetric.setFont(FontUtils.createRobotoFont("regular", 20f));
            btnAsymmetric.setPreferredSize(new Dimension(140, 30));
            btnAsymmetric.addActionListener(this.listener);
            pnlRow3Btn.add(btnAsymmetric);
        }
        pnlRow3.add(pnlRow3Btn, BorderLayout.CENTER);
        this.add(pnlRow3);
    }

    /**
     * renderHashRow   cài đặt giao diện giải thuật hash
     */
    private void renderHashRow() {
        JPanel pnlRow4 = new JPanel(new BorderLayout());
        pnlRow4.setPreferredSize(new Dimension(0, 70));
        pnlRow4.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        pnlRow4.setBackground(Color.WHITE);
        // JLabel
        JLabel lblRow4Type = new JLabel("GIẢI THUẬT HASH", JLabel.CENTER);
        lblRow4Type.setFont(FontUtils.createRobotoFont("medium", 24f));
        lblRow4Type.setForeground(Color.WHITE);
        lblRow4Type.setBackground(Color.decode("#10375C"));
        lblRow4Type.setOpaque(true);
        lblRow4Type.setPreferredSize(new Dimension(400, 0));
        pnlRow4.add(lblRow4Type, BorderLayout.WEST);
        // các JButton
        JPanel pnlRow4Btn = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        pnlRow4Btn.setBackground(Color.decode("#F4F6FF"));
        pnlRow4Btn.setBorder(BorderFactory.createEmptyBorder(0, 100, 0, 100));
        JButton btnHash;
        ArrayList<String> hashAlgorithmList = new ArrayList<>();
        hashAlgorithmList.addAll(Arrays.asList(CipherSupport.HASH_ALGORITHMS));
        hashAlgorithmList.addAll(Arrays.asList(CipherSupport.HASH_ALGORITHMS_THIRD_PARTY));
        for(String c: hashAlgorithmList) {
            btnHash = new JButton(c);
            btnHash.setBackground(Color.WHITE);
            btnHash.setFont(FontUtils.createRobotoFont("regular", 20f));
            btnHash.setPreferredSize(new Dimension(140, 30));
            btnHash.addActionListener(this.listener);
            pnlRow4Btn.add(btnHash);
        }
        pnlRow4.add(pnlRow4Btn, BorderLayout.CENTER);
        this.add(pnlRow4);
    }

    /**
     * renderDigitalSignatureRow    cài đặt giao diện chữ ký điện tử
     */
    private void renderDigitalSignatureRow() {
        JPanel pnlRow5 = new JPanel(new BorderLayout());
        pnlRow5.setPreferredSize(new Dimension(0, 70));
        pnlRow5.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        pnlRow5.setBackground(Color.WHITE);
        // JLabel
        JLabel lblRow5Type = new JLabel("CHỮ KÝ ĐIỆN TỬ", JLabel.CENTER);
        lblRow5Type.setFont(FontUtils.createRobotoFont("medium", 24f));
        lblRow5Type.setForeground(Color.WHITE);
        lblRow5Type.setBackground(Color.decode("#10375C"));
        lblRow5Type.setOpaque(true);
        lblRow5Type.setPreferredSize(new Dimension(400, 0));
        pnlRow5.add(lblRow5Type, BorderLayout.WEST);
        // các JButton
        JPanel pnlRow5Btn = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        pnlRow5Btn.setBackground(Color.decode("#F4F6FF"));
        JButton btnDigitalSignature = new JButton("Chữ ký điện tử");
        btnDigitalSignature.setActionCommand("DigitalSignature");
        btnDigitalSignature.setBackground(Color.WHITE);
        btnDigitalSignature.setFont(FontUtils.createRobotoFont("regular", 20f));
        btnDigitalSignature.setPreferredSize(new Dimension(200, 30));
        btnDigitalSignature.addActionListener(this.listener);
        pnlRow5Btn.add(btnDigitalSignature);
        pnlRow5.add(pnlRow5Btn, BorderLayout.CENTER);
        this.add(pnlRow5);
    }
}