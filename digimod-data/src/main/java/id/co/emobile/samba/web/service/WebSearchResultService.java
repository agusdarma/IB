package id.co.emobile.samba.web.service;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import id.co.emobile.samba.web.data.LinkTableVO;
import id.co.emobile.samba.web.data.WebConstants;
import id.co.emobile.samba.web.data.WebSearchResultVO;
import id.co.emobile.samba.web.data.param.ParamPagingVO;

@Service
public class WebSearchResultService {
	private static final Logger LOGGER = LoggerFactory.getLogger(WebSearchResultService.class);
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired
	private BizMessageService messageService;

	public HashMap<String, Integer> calculatePaging(int currentPage, int rowPerPage) {
		int rowStart=((currentPage-1)*rowPerPage)+1;
		int rowEnd=(currentPage*rowPerPage);
		HashMap<String, Integer> rowMap=new HashMap<String, Integer>();
		rowMap.put("rowStart", rowStart);
		rowMap.put("rowEnd", rowEnd);
		return rowMap;
	}
	
	private WebSearchResultVO fillSearchResultVO(String title, String[] arrayHeader, String[] arrayBody, 
			String[] arrayDbVariable, String bodyContent, int currentPage, 
			int totalRow, List<LinkTableVO> listLink,  Locale language, 
			int listSize, ParamPagingVO paramVO) throws Exception
	{

		String[] arrayRowPerPage=WebConstants.ARRAY_PAGING;
		int maxPage=(totalRow + (paramVO.getRowPerPage()-1))/paramVO.getRowPerPage();
		int panelPosition=0;		
		if(maxPage==1)
		{
			panelPosition=0;
		}
		if(currentPage==1 & maxPage > 1)
		{
			panelPosition=1;
		}
		if(currentPage > 1 & maxPage > currentPage)
		{
			panelPosition=2;
		}
		if(maxPage==currentPage & maxPage > 1)
		{
			panelPosition=3;
		}
		WebSearchResultVO wsr = new WebSearchResultVO();
		String header = objectMapper.writeValueAsString(arrayHeader);
		String body = objectMapper.writeValueAsString(arrayBody);
		String listRowPerPage = objectMapper.writeValueAsString(arrayRowPerPage);
		String dbVariable = objectMapper.writeValueAsString(arrayDbVariable);
		wsr.setHeader(header);
		wsr.setBody(body);
		wsr.setBodyContent(bodyContent);
		wsr.setSortVariable(dbVariable);
		wsr.setTitle(title);
		wsr.setTotalRow(totalRow);
		wsr.setTotalPage(maxPage);
		wsr.setCurrentPage(currentPage);
		wsr.setRowPerPage(paramVO.getRowPerPage());
		wsr.setPanelRowPerPage(listRowPerPage);
		wsr.setRowStart(paramVO.getRowStart());
		wsr.setRowEnd((paramVO.getRowStart()-1)+listSize);
		wsr.setPanelPosition(panelPosition);
		wsr.setLinkTable(listLink);
		if(listSize > 0)
		{
			wsr.setFooterPage(messageService.getMessageFor("l.footerPage", new String[]{String.valueOf(currentPage), String.valueOf(maxPage)}, language));
			wsr.setFooterRecord(messageService.getMessageFor("l.footerRecord", new String[]{String.valueOf(paramVO.getRowStart()), String.valueOf(paramVO.getRowStart()+listSize-1)}, language));	
		}
		else
		{
			wsr.setFooterPage(messageService.getMessageFor("l.footerNoResult", language));
			wsr.setFooterRecord("");
		}
		wsr.setChosenSortVariable(paramVO.getSortVariable());
		if(paramVO.getSortOrder().equals(WebConstants.SORT_ORDER_ASC))//0 ascending 1 descending
		{
			wsr.setSortOrder(0);
		}
		else
		{
			wsr.setSortOrder(1);
		}
		return wsr;
	}
	
	public String composeSearchResult(String title, String[] arrayHeader, String[] arrayBody, 
			String[] arrayDbVariable, String bodyContent, int currentPage, 
			int totalRow, List<LinkTableVO> listLink,  Locale language, 
			int listSize, ParamPagingVO paramVO)
	{
		try {
			WebSearchResultVO wsr = new WebSearchResultVO();
			wsr = fillSearchResultVO(title, arrayHeader, arrayBody, arrayDbVariable, 
					bodyContent, currentPage, totalRow, listLink, 
					language, listSize, paramVO);
			wsr.setUseExport(false);
			
			String result = objectMapper.writeValueAsString(wsr);
			return result;
		} catch (Exception e) {
			LOGGER.warn("Exception on composeSearchResult for " + title, e);
			return "";
		}
	}

	public String composeSearchResultWithExport(String title, String[] arrayHeader, String[] arrayBody, 
			String[] arrayDbVariable, String bodyContent, int currentPage, 
			int totalRow, List<LinkTableVO> listLink,  Locale language, 
			int listSize, ParamPagingVO paramVO)
	{
		try {
			WebSearchResultVO wsr=new WebSearchResultVO();
			wsr = fillSearchResultVO(title, arrayHeader, arrayBody, arrayDbVariable, 
					bodyContent, currentPage, totalRow, listLink, 
					language, listSize, paramVO);
			wsr.setUseExport(true);
			
			String result = objectMapper.writeValueAsString(wsr);
			return result;
		} catch (Exception e) {
			LOGGER.warn("Exception on composeSearchResultWithExport for " + title, e);
			return "";
		}
	}
	
}
