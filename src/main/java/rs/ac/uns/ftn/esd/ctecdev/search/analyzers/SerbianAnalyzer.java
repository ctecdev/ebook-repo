package rs.ac.uns.ftn.esd.ctecdev.search.analyzers;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.sr.SerbianNormalizationFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;

import rs.ac.uns.ftn.esd.ctecdev.search.filters.CyrilicToLatinFilter;

public class SerbianAnalyzer extends Analyzer {

    /**
     * An array containing some common Serbian words
     * that are usually not useful for searching.
     */
    public static final String[] STOP_WORDS =
    {
        "i","a","ili","ali","pa","te","da","u","po","na"
    };

    /**
     * Builds an analyzer.
     */
    public SerbianAnalyzer()
    {
    }

	@Override
	protected TokenStreamComponents createComponents(String fieldName) {
		// TODO Auto-generated method stub
		Tokenizer input = new StandardTokenizer();
		
		TokenStream result = new SerbianNormalizationFilter(input);
		//TokenStream result = new CyrilicToLatinFilter(input);
		
		result = new LowerCaseFilter(result);
		result = new StopFilter(result, StopFilter.makeStopSet(STOP_WORDS));
		return new TokenStreamComponents(input, result);
		
	}

}
