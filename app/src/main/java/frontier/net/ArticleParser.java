/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2015 Umeng, Inc
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package frontier.net;

import android.annotation.SuppressLint;
import android.text.TextUtils;


import com.dc.bookdemo.beans.Article;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import frontier.net.parser.RespParser;

/**
 * 将服务器返回的json数据转为文章列表的解析器
 *
 * 解析网络访问的结果
 *
 * 实现这个解析的接口
 */

@SuppressLint("SimpleDateFormat")
public class ArticleParser implements RespParser<List<Article>> {

    private static SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    //实现接口 ，解析成列表
    @SuppressLint("SimpleDateFormat")
    @Override
    public List<Article> parseResponse(String result) throws JSONException {

        JSONArray jsonArray = new JSONArray(result);
        List<Article> articleLists = new LinkedList<Article>();

        int count = jsonArray.length();
        for (int i = 0; i < count; i++) {
            JSONObject itemObject = jsonArray.optJSONObject(i);
            articleLists.add(parseItem(itemObject));
        }
        return articleLists;  //结果最后会通过接口传递
    }

    private Article parseItem(JSONObject itemObject) {
        Article articleItem = new Article();
        articleItem.title = itemObject.optString("title");
        articleItem.author = itemObject.optString("author");
        articleItem.post_id = itemObject.optString("post_id");
        String category = itemObject.optString("category");
        articleItem.category = TextUtils.isEmpty(category) ? 0 : Integer.valueOf(category);
        articleItem.publishTime = formatDate(dateformat, itemObject.optString("date"));
        return articleItem;
    }

    private static String formatDate(SimpleDateFormat dateFormat, String dateString) {
        try {
            Date date = dateFormat.parse(dateString);
            return dateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

}
