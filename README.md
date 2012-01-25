# README.md #

A project that helps create [FilterReader](http://docs.oracle.com/javase/6/docs/api/java/io/FilterReader.html) that removes and transforms lines before reading them.

## Usage ##

	public class UsageExample {

        public void test(final File file) throws IOException {
        	final MogrifiedReaderMaker maker = new MogrifiedReaderMaker();
        	
			maker.omitLines().startingWith("#");
        	maker.omitLines().containing("needle");
        	maker.transformLines().byReplacing("foobar", " foobaz");
        	final Reader reader = maker.read(file);
        }
	}
