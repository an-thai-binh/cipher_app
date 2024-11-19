package view.signature;

import model.signature.DigitalSignature;

import javax.swing.*;

public class DigitalSignatureView extends JPanel {
    private DigitalSignature ds;
    public DigitalSignatureView(DigitalSignature ds) {
        this.ds = ds;
    }
}
