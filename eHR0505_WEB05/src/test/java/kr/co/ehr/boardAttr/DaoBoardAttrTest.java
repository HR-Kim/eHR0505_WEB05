package kr.co.ehr.boardAttr;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import kr.co.ehr.board.service.Board;
import kr.co.ehr.board.service.impl.BoardDaoImpl;
import kr.co.ehr.boardAttr.service.BoardAttr;
import kr.co.ehr.boardAttr.service.impl.BoardAttrDaoImpl;
import kr.co.ehr.code.service.Code;
import kr.co.ehr.code.service.impl.CodeDaoImpl;
import kr.co.ehr.user.service.Search;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/**/*.xml"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)//@Test NAME_ASCENDING으로 수행.
public class DaoBoardAttrTest {
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());	
	
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private BoardAttrDaoImpl boardAttrDaoImpl;
	
	
	List<BoardAttr> list;
	
	@Before
	public void setUp() {
		//	public BoardAttr(String boardId, String title, String contents, int readCnt, String fileId, String regId,String regDt) {
		list = Arrays.asList(
				 new BoardAttr("noBoardId","J01제목_124","J01내용_124",0,"1","admin","no_date")
				,new BoardAttr("noBoardId","J02제목_124","J02내용_124",0,"1","admin","no_date")
				,new BoardAttr("noBoardId","J03제목_124","J03내용_124",0,"1","admin","no_date")
				,new BoardAttr("noBoardId","J04제목_124","J04내용_124",0,"1","admin","no_date")
				,new BoardAttr("noBoardId","J05제목_124","J05내용_124",0,"1","admin","no_date")
				);
		
	}
	
	@Test
	public void get_retrieve() {
		LOG.debug("======================================");
		LOG.debug("=01. 기존 데이터 삭제=");
		LOG.debug("======================================");	
		Search search=new Search();
		search.setSearchWord("_124");
		List<BoardAttr> getList = (List<BoardAttr>) boardAttrDaoImpl.get_boardIdList(search);
		for(BoardAttr vo:getList) {
			boardAttrDaoImpl.do_delete(vo);
		}	
		
		LOG.debug("======================================");
		LOG.debug("=02. 데이터 추가=");
		LOG.debug("======================================");	
		for(BoardAttr vo:list) { 
			int flag = boardAttrDaoImpl.do_save(vo);
			assertThat(1, is(flag));
		}		
		
		//=====================================
		//2.01 등록Data조회
		//=====================================
		search.setSearchDiv("10");
		search.setPageSize(10);
		search.setPageNum(1);
		
		List<BoardAttr> addlistData = (List<BoardAttr>) boardAttrDaoImpl.get_retrieve(search);
		assertThat(5, is(addlistData.size()));		
	}
	
	
	@Test
	public void do_update() {
		LOG.debug("======================================");
		LOG.debug("=01. 기존 데이터 삭제=");
		LOG.debug("======================================");
		Search search=new Search();
		search.setSearchWord("_124");
		List<BoardAttr> getList = (List<BoardAttr>) boardAttrDaoImpl.get_boardIdList(search);
		for(BoardAttr vo:getList) {
			boardAttrDaoImpl.do_delete(vo);
		}		
		
		LOG.debug("======================================");
		LOG.debug("=02. 데이터 추가=");
		LOG.debug("======================================");
		for(BoardAttr vo:list) { 
			int flag = boardAttrDaoImpl.do_save(vo);
			assertThat(1, is(flag));
		}		

		//=====================================
		//2.01 등록Data조회
		//=====================================
		List<BoardAttr> addlistData = (List<BoardAttr>) boardAttrDaoImpl.get_boardIdList(search);
		assertThat(5, is(addlistData.size()));
		LOG.debug("======================================");
		LOG.debug("=03. 데이터 수정=");
		LOG.debug("======================================");
		BoardAttr  changeData = addlistData.get(0);
		changeData.setTitle(changeData.getTitle()+"_U");
		changeData.setContents(changeData.getContents()+"_U");
		changeData.setRegId(changeData.getRegId()+"_U");
		
		LOG.debug("======================================");
		LOG.debug("=03.01 데이터 수정=");
		LOG.debug("======================================");			
		int flag = boardAttrDaoImpl.do_update(changeData);
		assertThat(1, is(flag));		
		
		LOG.debug("======================================");
		LOG.debug("=04. 등록 DATA조회=");
		LOG.debug("======================================");			
		addlistData = (List<BoardAttr>) boardAttrDaoImpl.get_boardIdList(search);
		
		LOG.debug("======================================");
		LOG.debug("=05. 비교=");
		LOG.debug("======================================");
		this.checkData(changeData, addlistData.get(0));
	}
	
	@Test
	public void addAndGet() {
		//=====================================
		//0. 기존data삭제
		//=====================================		
		Search search=new Search();
		search.setSearchWord("_124");
		List<BoardAttr> getList = (List<BoardAttr>) boardAttrDaoImpl.get_boardIdList(search);
		for(BoardAttr vo:getList) {
			boardAttrDaoImpl.do_delete(vo);
		}
		
		
		//=====================================
		//1. Data등록
		//=====================================
		for(BoardAttr vo:list) { 
			int flag = boardAttrDaoImpl.do_save(vo);
			assertThat(1, is(flag));
		}
		
		//=====================================
		//2. 등록Data조회
		//=====================================
		List<BoardAttr> addlistData = (List<BoardAttr>) boardAttrDaoImpl.get_boardIdList(search);
		assertThat(5, is(addlistData.size()));
		
		
		//=====================================
		//3. 비교:boardId는 자동증가로 비교 할수 없음.(제목,내용,등록자)
		//=====================================		
		for(int i=0;i<list.size();i++) {
			checkData(addlistData.get(i),list.get(i));
		}
	}
	
	private void checkData(BoardAttr org,BoardAttr vs) {
		assertThat(org.getTitle(), is(vs.getTitle()));
		assertThat(org.getContents(), is(vs.getContents()));
		assertThat(org.getRegId(), is(vs.getRegId()));
		
	}
	
	
	
	@Test
	public void getBean() {
		LOG.debug("====================");
		LOG.debug("=context="+context);
		LOG.debug("=boardDaoImpl="+boardAttrDaoImpl);
		LOG.debug("====================");
		assertThat(context,  is(notNullValue()));
		assertThat(boardAttrDaoImpl,  is(notNullValue()));
	}
	
	
	@After
	public void tearDown() {
	
	}
	
	
	
}


