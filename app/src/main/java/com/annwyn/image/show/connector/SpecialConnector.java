package com.annwyn.image.show.connector;

import com.annwyn.image.show.model.Special;

import java.util.List;

public interface SpecialConnector extends BaseConnector {

    void loadDataComplete(List<Special> specials);

    void setPageNumber(int pageNumber);

    void loadComplete();
}
