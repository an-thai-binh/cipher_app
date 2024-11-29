package view;

import utils.ArrayUtils;
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
        renderContent();
    }

    /**
     * renderSelf   cài đặt giao diện chính
     */
    private void renderSelf() {
        this.setLayout(new BorderLayout());
        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
    }

    /**
     * renderTitleRow   cài đặt giao diện dòng tiêu đề
     */
    private void renderTitleRow() {
        // tiêu đề
        JLabel lblMainTitle = new JLabel("CÔNG CỤ BẢO MẬT", JLabel.CENTER);
        lblMainTitle.setFont(FontUtils.createRobotoFont("extraBold", 30f));
        lblMainTitle.setForeground(Color.decode("#EB8317"));
        lblMainTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblMainTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        this.add(lblMainTitle, BorderLayout.NORTH);
    }

    /**
     * renderContent    cài đặt giao diện nội dung
     */
    private void renderContent() {
        JPanel pnlContent = new JPanel();
        pnlContent.setLayout(new BoxLayout(pnlContent, BoxLayout.Y_AXIS));
        pnlContent.setBackground(null);
        // classical, symmetric, hash
        JPanel pnlTop = new JPanel(new GridLayout(1, 3, 20, 0));
        pnlTop.setBackground(null);
        renderMenuAlgorithm("Giải thuật cổ điển", CipherSupport.CLASSICAL_CIPHERS, pnlTop);
        renderMenuAlgorithm("Giải thuật đối xứng hiện đại", ArrayUtils.concatenate(CipherSupport.SYMMETRIC_CIPHERS, CipherSupport.SYMMETRIC_CIPHERS_THIRD_PARTY), pnlTop);
        renderMenuAlgorithm("Giải thuật Hash", ArrayUtils.concatenate(CipherSupport.HASH_ALGORITHMS, CipherSupport.HASH_ALGORITHMS_THIRD_PARTY), pnlTop);
        pnlContent.add(pnlTop);
        // asymmetric
        renderSingleAlgorithm("Giải thuật bất đối xứng", new String[]{"RSA"}, pnlContent);
        // digital signature
        renderSingleAlgorithm("Chữ ký điện tử", "Chữ ký điện tử", "DigitalSignature", pnlContent);
        // add
        this.add(pnlContent, BorderLayout.CENTER);
    }

    /**
     * renderMenuAlgorithm   cài đặt menu các thuật toán
     * @param name tên thuật toán
     * @param algorithms    danh sách thuật toán
     * @param panel    parent panel
     */
    private void renderMenuAlgorithm(String name, String[] algorithms, JPanel panel) {
        JPanel pnlMenu = new JPanel(new BorderLayout());
        pnlMenu.setBackground(null);
        pnlMenu.setPreferredSize(new Dimension(0, 420));
        // title
        JPanel pnlMenuTitle = new JPanel(new GridLayout(1, 1));
        pnlMenuTitle.setBackground(Color.decode("#10375C"));
        JLabel lblMenuTitle = new JLabel(name, JLabel.CENTER);
        lblMenuTitle.setFont(FontUtils.createRobotoFont("medium", 20f));
        lblMenuTitle.setForeground(Color.WHITE);
        lblMenuTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        pnlMenuTitle.add(lblMenuTitle);
        pnlMenu.add(pnlMenuTitle, BorderLayout.NORTH);
        // content (tham khao: https://stackoverflow.com/questions/65132573/adding-jscrollpane-to-jpanel-with-another-panels-inside)
        Box menuContainer = Box.createVerticalBox();    // container
        JButton btnAlgorithm;
        for(String a: algorithms) {
            JPanel pnlAlgorithm = new JPanel(new GridLayout(1, 1));
            pnlAlgorithm.setBackground(null);
            pnlAlgorithm.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
            btnAlgorithm = new JButton(a);
            btnAlgorithm.setBackground(Color.WHITE);
            btnAlgorithm.setFont(FontUtils.createRobotoFont("regular", 20f));
            btnAlgorithm.addActionListener(this.listener);
            pnlAlgorithm.add(btnAlgorithm);
            menuContainer.add(pnlAlgorithm);
        }
        JPanel pnlAlgorithmWrapper = new JPanel(new BorderLayout());    // wrapper
        pnlAlgorithmWrapper.add(menuContainer, BorderLayout.PAGE_START);
        pnlAlgorithmWrapper.setBackground(Color.decode("#F4F6FF"));
        pnlAlgorithmWrapper.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        JScrollPane contentScrollPane = new JScrollPane(pnlAlgorithmWrapper);   // scroll pane
        contentScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        contentScrollPane.setPreferredSize(new Dimension(0, 200));
        pnlMenu.add(contentScrollPane, BorderLayout.CENTER);
        panel.add(pnlMenu);
    }

    /**
     * renderSingleAlgorithm    cài đặt dòng thuật toán
     * @param name  tên thuật toán
     * @param algorithms    danh sách thuật toán
     * @param panel parent panel
     */
    private void renderSingleAlgorithm(String name, String[] algorithms, JPanel panel) {
        JPanel pnlMenu = new JPanel(new BorderLayout());
        pnlMenu.setBackground(null);
        pnlMenu.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        // title
        JPanel pnlMenuTitle = new JPanel(new GridLayout(1, 1));
        pnlMenuTitle.setBackground(Color.decode("#10375C"));
        JLabel lblMenuTitle = new JLabel(name, JLabel.CENTER);
        lblMenuTitle.setFont(FontUtils.createRobotoFont("medium", 20f));
        lblMenuTitle.setForeground(Color.WHITE);
        lblMenuTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        pnlMenuTitle.add(lblMenuTitle);
        pnlMenu.add(pnlMenuTitle, BorderLayout.NORTH);
        // content
        JPanel pnlMenuContent = new JPanel(new GridLayout(1, 1));
        JPanel pnlContent = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlContent.setBackground(Color.decode("#F4F6FF"));
        JButton btnAlgorithm;
        for(String a: algorithms) {
            btnAlgorithm = new JButton(a);
            btnAlgorithm.setPreferredSize(new Dimension(300, 50));
            btnAlgorithm.setBackground(Color.WHITE);
            btnAlgorithm.setFont(FontUtils.createRobotoFont("regular", 20f));
            btnAlgorithm.addActionListener(this.listener);
            pnlContent.add(btnAlgorithm);
        }
        pnlMenuContent.add(pnlContent);
        pnlMenu.add(pnlMenuContent, BorderLayout.CENTER);
        panel.add(pnlMenu);
    }

    /**
     * renderSingleAlgorithm    cài đặt dòng thuật toán chỉ có 1 thuật toán
     * @param name  tên thuật toán
     * @param algorithm thuật toán
     * @param actionCommand action command
     * @param panel parent panel
     */
    private void renderSingleAlgorithm(String name, String algorithm, String actionCommand, JPanel panel) {
        JPanel pnlMenu = new JPanel(new BorderLayout());
        pnlMenu.setBackground(null);
        pnlMenu.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        // title
        JPanel pnlMenuTitle = new JPanel(new GridLayout(1, 1));
        pnlMenuTitle.setBackground(Color.decode("#10375C"));
        JLabel lblMenuTitle = new JLabel(name, JLabel.CENTER);
        lblMenuTitle.setFont(FontUtils.createRobotoFont("medium", 20f));
        lblMenuTitle.setForeground(Color.WHITE);
        lblMenuTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        pnlMenuTitle.add(lblMenuTitle);
        pnlMenu.add(pnlMenuTitle, BorderLayout.NORTH);
        // content
        JPanel pnlMenuContent = new JPanel(new GridLayout(1, 1));
        JPanel pnlContent = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlContent.setBackground(Color.decode("#F4F6FF"));
        JButton btnAlgorithm = new JButton(algorithm);
        btnAlgorithm.setActionCommand(actionCommand);
        btnAlgorithm.setPreferredSize(new Dimension(300, 50));
        btnAlgorithm.setBackground(Color.WHITE);
        btnAlgorithm.setFont(FontUtils.createRobotoFont("regular", 20f));
        btnAlgorithm.addActionListener(this.listener);
        pnlContent.add(btnAlgorithm);
        pnlMenuContent.add(pnlContent);
        pnlMenu.add(pnlMenuContent, BorderLayout.CENTER);
        panel.add(pnlMenu);
    }
}
