package com.annwyn.image.show.connector;

import com.annwyn.image.show.model.Detail;
import com.annwyn.image.show.model.Special;

import java.util.List;

public interface HomeConnector extends BaseConnector {

    void initializeBanner(List<Special> specials);

    void loadDataComplete(List<Detail> details);

    void setPageNumber(int pageNumber);

    void loadComplete();
}
