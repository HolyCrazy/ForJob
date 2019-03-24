package com.example.leeduo.forjob.JsonBean;

/**
 * Created by LeeDuo on 2019/3/5.
 */

public class JsonRecommendHit {
    private String _id;
    private String _index;
    private String _type;
    private JsonRecommendHitSource _source;
    private double _score;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_index() {
        return _index;
    }

    public void set_index(String _index) {
        this._index = _index;
    }

    public String get_type() {
        return _type;
    }

    public void set_type(String _type) {
        this._type = _type;
    }

    public JsonRecommendHitSource get_source() {
        return _source;
    }

    public void set_source(JsonRecommendHitSource _source) {
        this._source = _source;
    }

    public double get_score() {
        return _score;
    }

    public void set_score(double _score) {
        this._score = _score;
    }
}
