package view.hash;

import model.hash.IHashAlgorithm;
import utils.CipherSupport;
import utils.FontUtils;
import utils.IconUtils;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

public class HashAlgorithmView extends JPanel {
    private final IHashAlgorithm algorithm;
    private CardLayout cardLayout;
    private JTextField txtFieldInput;
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
            if(algorithm.getName().equals("SHA")) {
                renderModeRow();
            }
            renderChooseInputFileRow();
            renderInputFileRow();
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
            JLabel lblMode = new JLabel("Chọn giải thuật:", JLabel.CENTER);
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
         * renderChooseInputFileRow   cài đặt giao diện dòng chọn input file
         */
        private void renderChooseInputFileRow() {
            JPanel pnlInputFile = new JPanel(new GridLayout(1, 1));
            pnlInputFile.setBackground(null);
            pnlInputFile.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
            // button input file
            JButton btnInputFile = new JButton("CHỌN FILE ĐẦU VÀO");
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
                        java.util.List<File> droppedFiles = (List<File>) dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
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
         * renderActionRow  cài đặt giao diện dòng action
         */
        private void renderActionRow() {
            JPanel pnlAction = new JPanel(new FlowLayout(FlowLayout.CENTER));
            pnlAction.setBackground(null);
            pnlAction.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
            JButton btnHash = new JButton("Hash");
            btnHash.setActionCommand(isTextPanel ? "hash" : "hashFile");
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
                        txtAreaOutput.setText(algorithm.hash(text));
                    } catch (Exception ex) {
                        showErrorDialog(ex.getMessage());
                    }
                    break;
                }
                case "chooseInput": {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setDialogTitle("Chọn file thao tác");
                    int userOption = fileChooser.showOpenDialog(HashAlgorithmView.this.getParent());
                    if(userOption == JFileChooser.APPROVE_OPTION) {
                        File inputFile = fileChooser.getSelectedFile();
                        txtFieldInput.setText(inputFile.getAbsolutePath());
                    }
                    break;
                }
                case "hashFile": {
                    String src = txtFieldInput.getText();
                    // set instance
                    try {
                        String name = algorithm.getName();
                        if(name.equals("SHA")) {    // nếu là SHA thì lấy instance từ cbbMode
                            algorithm.setInstance(cbbMode.getSelectedItem().toString());
                        } else {
                            algorithm.setInstance(algorithm.getName());
                        }
                        txtAreaOutput.setText(algorithm.hashFile(src));
                    } catch (Exception ex) {
                        showErrorDialog(ex.getMessage());
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
}
