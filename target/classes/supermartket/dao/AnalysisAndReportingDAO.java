package supermartket.dao;

import java.util.List;
import supermartket.dao.dto.AnalysisAndReportingDTO;
import supermartket.dao.dto.CustomerAnalysisAndReportingDTO;
import supermartket.dao.dto.ItemProductAnalysisAndReportingDTO;

public interface AnalysisAndReportingDAO {
    AnalysisAndReportingDTO getInfoAnalysis();
    List<ItemProductAnalysisAndReportingDTO> getTopProductAnalysis();
    List<CustomerAnalysisAndReportingDTO> getTop5Customer();
}
