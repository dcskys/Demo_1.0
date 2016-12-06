package frontier.net;

import com.dc.bookdemo.beans.Article;

import org.json.JSONException;

import java.util.List;

import frontier.net.parser.RespParser;


public class StringParser implements RespParser<String> {
    @Override
    public String parseResponse(String result) throws JSONException {
        return result;
    }
}
