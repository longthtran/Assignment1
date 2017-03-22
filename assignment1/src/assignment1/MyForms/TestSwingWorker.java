package assignment1.MyForms;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public class TestSwingWorker {

    private JProgressBar progressBar;
    private static long noBlock;
    protected void initUI() {
        final JFrame frame = new JFrame();
        frame.setTitle(TestSwingWorker.class.getSimpleName());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JButton button = new JButton("Clik me to start work");
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                doWork();
            }
        });
        
        progressBar = new JProgressBar();
        frame.add(progressBar);
        frame.add(progressBar,BorderLayout.NORTH);
        frame.add(button, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);
        
        
		
    }

    protected void doWork() {
        SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {
            @Override
            protected Void doInBackground() throws Exception {
            	//change directory to run
            	//input to encrypt
            	String inputFile = "/home/pi/lab2.pdf";
            	//output file of encryption / input file of decryption
        		String outputFile = "/home/pi/lab2after";
        		//output file of decryption - not used yet
        		String outputFileAll = "/home/pi/lab2afterall.pdf";
        		//encryption
        		File f = new File(inputFile);
        		long fileSize = f.length();// byte
        		// key
        		KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        		SecretKey key = keyGenerator.generateKey();
        		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        		cipher.init(cipher.ENCRYPT_MODE, key);
        		long keySize = key.getEncoded().length;
        		Path path = Paths.get(inputFile);
        		byte[] data = Files.readAllBytes(path);
        		List<byte[]> devidedFile = divideArray(data, keySize);
        		FileOutputStream out = new FileOutputStream(new File(outputFile));
        		// BufferedWriter bw = new BufferedWriter(new FileWriter(f));
        		//long n = fileSize / keySize+1;//chia het ?? chia ko het
        		long n = noBlock+1;
        		double sumProcessing = 0;
        		long blockSizeSum = 0;
        		for (long i = 0; i < n; i++) {
        			// bw.write(text);
        			byte[] res = cipher.doFinal(devidedFile.get((int)i));
        			out.write(res);
        			// function to get block size -> blockSize
        			// long blockSize= 1024/8;
        			long blockSize = devidedFile.get((int)i).length;
        			// if(i%10==0) System.out.println("file size:" +blockSize);
        			blockSizeSum+=blockSize;
        			sumProcessing = (double) blockSizeSum / fileSize;
        			//setvalue here
        			String s = Objects.toString(blockSize, null);
        			String s1 = Objects.toString(sumProcessing, null);
        			System.out.println("Plain Text : " + n + s + " -- " + s1);
        			Thread.sleep(1);
        			int pass = (int)(sumProcessing*100);
        			System.out.println(pass);
        			publish(pass); 
        		}
        		out.close();
                // Here not in the EDT
                /*for (int i = 0; i < 300; i+=2) {
                    // Simulates work
                    Thread.sleep(1);
                    publish(i); // published values are passed to the #process(List) method
                }*/
                return null;
            }

            @Override
            protected void process(List<Integer> chunks) {
                // chunks are values retrieved from #publish()
                // Here we are on the EDT and can safely update the UI
                progressBar.setValue(chunks.get(chunks.size()-1));
            }

            @Override
            protected void done() {
                // Invoked when the SwingWorker has finished
                // We are on the EDT, we can safely update the UI
                //progressTextField.setText("Done");
            }
        };
        worker.execute();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override	
            public void run() {
                new TestSwingWorker().initUI();
            }
        });
    }
    public static List<byte[]> divideArray(byte[] source, long chunksize) {

		List<byte[]> result = new ArrayList<byte[]>();
		long start = 0;
		noBlock=0;
		while (start < source.length) {
			long end = Math.min(source.length, start + chunksize);
			result.add(Arrays.copyOfRange(source, (int) start, (int) end));
			start += chunksize;
			noBlock++;
		}

		return result;
	}
}