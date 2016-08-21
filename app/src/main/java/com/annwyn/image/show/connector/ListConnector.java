package com.annwyn.image.show.connector;

import com.annwyn.image.show.model.Detail;

import java.util.List;

public interface ListConnector extends BaseConnector {

    void loadDataComplete(List<Detail> details);

    void setPageNumber(int pageNumber);

    void loadComplete();
}
