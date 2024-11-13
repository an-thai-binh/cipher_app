package view;

import utils.CipherSupport;
import utils.FontUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

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
        renderSymmetricRow();
        renderSymmetricRow();
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
        // JLabel dòng 1
        JLabel lblRow1Type = new JLabel("MÃ HÓA CỔ ĐIỂN", JLabel.CENTER);
        lblRow1Type.setFont(FontUtils.createRobotoFont("medium", 24f));
        lblRow1Type.setForeground(Color.WHITE);
        lblRow1Type.setBackground(Color.decode("#10375C"));
        lblRow1Type.setOpaque(true);
        lblRow1Type.setPreferredSize(new Dimension(400, 0));
        pnlRow1.add(lblRow1Type, BorderLayout.WEST);
        // các JButton của dòng 1
        JPanel pnlRow1Btn = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        pnlRow1Btn.setBackground(Color.decode("#F4F6FF"));
        pnlRow1Btn.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
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
        // JLabel dòng 2
        JLabel lblRow2Type = new JLabel("MÃ HÓA ĐỐI XỨNG HIỆN ĐẠI", JLabel.CENTER);
        lblRow2Type.setFont(FontUtils.createRobotoFont("medium", 24f));
        lblRow2Type.setForeground(Color.WHITE);
        lblRow2Type.setBackground(Color.decode("#10375C"));
        lblRow2Type.setOpaque(true);
        lblRow2Type.setPreferredSize(new Dimension(400, 0));
        pnlRow2.add(lblRow2Type, BorderLayout.WEST);
        // các JButton của dòng 2
        JPanel pnlRow2Btn = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        pnlRow2Btn.setBackground(Color.decode("#F4F6FF"));
        JButton btnSymmetric;
        for(String c: CipherSupport.SYMMETRIC_CIPHERS) {
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
        // JLabel dòng 2
        JLabel lblRow3Type = new JLabel("MÃ HÓA BẤT ĐỐI XỨNG", JLabel.CENTER);
        lblRow3Type.setFont(FontUtils.createRobotoFont("medium", 24f));
        lblRow3Type.setForeground(Color.WHITE);
        lblRow3Type.setBackground(Color.decode("#10375C"));
        lblRow3Type.setOpaque(true);
        lblRow3Type.setPreferredSize(new Dimension(400, 0));
        pnlRow3.add(lblRow3Type, BorderLayout.WEST);
        // các JButton của dòng 2
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
}
