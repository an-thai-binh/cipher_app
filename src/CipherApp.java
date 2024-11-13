import controller.CipherController;
import controller.ICipherController;
import model.CipherModel;
import model.ICipherModel;
import view.CipherView;
import view.ICipherView;

public class CipherApp {
	public static void main(String[] args) {
		ICipherView view = new CipherView();
		ICipherModel model = new CipherModel();
		ICipherController controller = new CipherController(view, model);
		controller.start();
    }
}