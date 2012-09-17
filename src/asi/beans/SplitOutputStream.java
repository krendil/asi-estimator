package asi.beans;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 
 * Forwards all data to a number of other OutputStreams 
 * 
 * @author David Osborne
 *
 */
public class SplitOutputStream extends OutputStream {

	private OutputStream[] streams;
	
	public SplitOutputStream(OutputStream ... streams) {
		this.streams = streams;
	}
	
	@Override
	public void write(int b) throws IOException {
		for( OutputStream stream : streams) {
			stream.write(b);
		}
	}

	@Override
	public void close() throws IOException {
		for( OutputStream stream : streams) {
			stream.close();
		}
	}

	@Override
	public void flush() throws IOException {
		for( OutputStream stream : streams) {
			stream.flush();
		}
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		for( OutputStream stream : streams) {
			stream.write(b, off, len);
		}
	}

	@Override
	public void write(byte[] b) throws IOException {
		for( OutputStream stream : streams) {
			stream.write(b);
		}
	}
	
	

}
