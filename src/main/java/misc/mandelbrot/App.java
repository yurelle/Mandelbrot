package misc.mandelbrot;

public class App {
	public static final AppContext APP_CONTEXT = new AppContext();
	
	public App() {
		//Create App Window
		APP_CONTEXT.appWindow = new MainWindow();
		
		//Trigger first run of the data
		APP_CONTEXT.rebuildPixelData();
	}
	
    public static void main(String[] args) {
    	new App();
    }
}
