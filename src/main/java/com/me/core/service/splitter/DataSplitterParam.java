package com.me.core.service.splitter;

import com.me.core.domain.entities.DataSet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataSplitterParam {
    private MainDataSplitParams mainParams;
    private DataSet dataSet;
    private List<String> categories;
}
