package ju.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ju.dto.BookDTO;
import ju.dto.LoanDTO;
import ju.dto.MemberDTO;
import ju.dto.YeyakDTO;
import ju.model.BookDAO;
import ju.model.EmailDAO;
import ju.model.FedexDAO;
import ju.model.LoanDAO;
import ju.model.MemberDAO;
import ju.model.YeyakDAO;

@Controller
public class AdminLoanBookController {

	@Autowired
	public LoanDAO loanDao;	
	
	@Autowired
	public BookDAO bookDao;
	
	@Autowired
	public YeyakDAO yeyakDao;
	
	@Autowired
	public FedexDAO fedexDao;
	
	@Autowired
	public MemberDAO memberDao;

	
	// 대출관리 메인페이지로 이동
		@RequestMapping("/loanbookList.ju")
		public ModelAndView loanbookList(
				@RequestParam(value="cp",defaultValue="1")int cp,
				@RequestParam(value="cp2",defaultValue="1")int cp2,
				@RequestParam(value="tagNum",defaultValue="0")int value){
			int totalCnt = 0;
			switch(value){
			case 0: totalCnt = loanDao.getTotalCnt(); ; break;
			case 1: totalCnt = loanDao.getTotalCntBook(); break;
			case 2: totalCnt = loanDao.getTotalCntFedex(); break;
			}
			int totalCnt2 = loanDao.getTotalCntReturn();
			totalCnt = totalCnt==0?1:totalCnt; // 0이면 1을 반환해주도록 검증
			int listSize = 5;
			int pageSize = 5;
			String pageStr = ju.page.PageModule.pageMakeValueSearch("loanbookList.ju",value, totalCnt, listSize, pageSize, cp); // 페이징을 위해 저장
			String pageStr2 = ju.page.PageModule.pageMake2("loanbookList.ju", totalCnt2, listSize, pageSize, cp2); // 페이징을 위해 저장
			List<LoanDTO> list = loanDao.loanList(cp, listSize);
			List<LoanDTO> list2 = loanDao.loanListAfter(cp2, listSize);
			String dateFormat="yyyy-MM-dd";
			SimpleDateFormat sdf=new SimpleDateFormat(dateFormat);
			for(int i=0; i<list.size(); i++){
				String sdDay = sdf.format(list.get(i).getLb_sd());
				list.get(i).setLb_sday(sdDay);
				String edDay = sdf.format(list.get(i).getLb_ed());
				list.get(i).setLb_eday(edDay);
				
				String bk_isbn = list.get(i).getBk_isbn();
				System.out.println(bk_isbn);
				int count = loanDao.yeyakNum(bk_isbn);
				System.out.println(count);
				list.get(i).setBk_yeyak(count);
				int info = list.get(i).getLb_return();
				
				switch(info){
				case 1: list.get(i).setLb_returnStr("일반대출"); break;
				case 2: list.get(i).setLb_returnStr("택배대출"); break;
				}
			}
			
			for(int i=0; i<list2.size(); i++){
				int info = list2.get(i).getLb_return();
				
				switch(info){
				case 0: list2.get(i).setLb_returnStr("반납됨"); break;
				}
				
				String sdDay = sdf.format(list2.get(i).getLb_sd());
				list2.get(i).setLb_sday(sdDay);
				String edDay = sdf.format(list2.get(i).getLb_ed());
				list2.get(i).setLb_eday(edDay);
			}
			
			ModelAndView mav = new ModelAndView("admin/loanbookManage/loanbookList","list",list);
			mav.addObject("list2",list2);
			mav.addObject("pageStr",pageStr);
			mav.addObject("pageStr2",pageStr2);
			return mav;
		}
		
	// 대출관리 서치박스 메인페이지로 이동
			@RequestMapping("/loanBookSelList.ju")
			public ModelAndView loanbookSelList(
					@RequestParam(value="cp",defaultValue="1")int cp,
					@RequestParam(value="cp2",defaultValue="1")int cp2,
					@RequestParam(value="tagNum",defaultValue="0")int value){
				int totalCnt = 0;
				switch(value){
				case 0: totalCnt = loanDao.getTotalCnt(); ; break;
				case 1: totalCnt = loanDao.getTotalCntBook(); break;
				case 2: totalCnt = loanDao.getTotalCntFedex(); break;
				}
				System.out.println(value);
				System.out.println(totalCnt);
				 // 페이징을 위해
				totalCnt = totalCnt==0?1:totalCnt; // 0이면 1을 반환해주도록 검증
				int totalCnt2 = loanDao.getTotalCntReturn();
				int listSize = 5;
				int pageSize = 5;
				List<LoanDTO> list = null;
				switch(value){
				case 0: list = loanDao.loanList(cp, listSize); break;
				case 1: list = loanDao.loanListBook(cp, listSize); break;
				case 2: list = loanDao.loanListFedex(cp, listSize); break;
				}
				String pageStr = ju.page.PageModule.pageMakeValueSearch("loanBookSelList.ju",value, totalCnt, listSize, pageSize, cp); // 페이징을 위해 저장
				String pageStr2 = ju.page.PageModule.pageMake2("loanbookList.ju", totalCnt2, listSize, pageSize, cp2); // 페이징을 위해 저장
				List<LoanDTO> list2 = loanDao.loanListAfter(cp2, listSize);
				String dateFormat="yyyy-MM-dd";
				SimpleDateFormat sdf=new SimpleDateFormat(dateFormat);
				for(int i=0; i<list.size(); i++){
					String sdDay = sdf.format(list.get(i).getLb_sd());
					list.get(i).setLb_sday(sdDay);
					String edDay = sdf.format(list.get(i).getLb_ed());
					list.get(i).setLb_eday(edDay);
					
					String bk_isbn = list.get(i).getBk_isbn();
					System.out.println(bk_isbn);
					int count = loanDao.yeyakNum(bk_isbn);
					System.out.println(count);
					list.get(i).setBk_yeyak(count);
					int info = list.get(i).getLb_return();
						
					switch(info){
					case 1: list.get(i).setLb_returnStr("일반대출"); break;
					case 2: list.get(i).setLb_returnStr("택배대출"); break;
					}
				}
				
				for(int i=0; i<list2.size(); i++){
					int info = list2.get(i).getLb_return();
						
					switch(info){
					case 0: list2.get(i).setLb_returnStr("반납됨"); break;
					}
					
					String sdDay = sdf.format(list2.get(i).getLb_sd());
					list2.get(i).setLb_sday(sdDay);
					String edDay = sdf.format(list2.get(i).getLb_ed());
					list2.get(i).setLb_eday(edDay);
				}
					
				ModelAndView mav = new ModelAndView("admin/loanbookManage/loanbookList","list",list);
				mav.addObject("list2",list2);
				mav.addObject("pageStr",pageStr);
				mav.addObject("pageStr2",pageStr2);
				return mav;
			}
		
	// 반납페이지로 이동
		@RequestMapping(value="/checkIn.ju",method=RequestMethod.GET)
		public ModelAndView checkIn(){
			ModelAndView mav = new ModelAndView();
			mav.setViewName("admin/loanbookManage/checkIn");
			return mav;
		}
		
	// 대출페이지로 이동
		@RequestMapping(value="/checkOut.ju",method=RequestMethod.GET)
		public ModelAndView checkOut(){
			ModelAndView mav = new ModelAndView();
			mav.setViewName("admin/loanbookManage/checkOut");
			return mav;
		}
		
	// 반납도서 정보페이지로 이동
		@RequestMapping(value="/loanbookInfo2.ju",method=RequestMethod.GET)
		public ModelAndView loanbookInfo2(String bk_idx){
			BookDTO dto = bookDao.bookReInfo(bk_idx);
			ModelAndView mav = null;
			if(dto==null){
				String msg = "대출된 책이 아닙니다. 다시 확인해 주세요";
				 mav = new ModelAndView("admin/adminMsg","msg",msg);
			}else{
				mav = new ModelAndView("admin/loanbookManage/loanbookInfo","dto",dto);
			}
			return mav;
		}
				
	// 대출하기
		@RequestMapping(value="/checkOut2.ju",method=RequestMethod.GET)
		public ModelAndView checkOutStart(
				@RequestParam(value="bk_idx",defaultValue="0")String bk_idx,
				LoanDTO dto,HttpSession session){
			ModelAndView mav = null;
			int count = loanDao.loanOne(bk_idx);
			BookDTO dto2 = bookDao.bookInfo(bk_idx);
			String isbn = dto2.getBk_isbn();
			System.out.println(isbn);
			System.out.println(count);
			if(count==0){
				MemberDTO mdto=(MemberDTO)session.getAttribute("dto");
				String mem_idx=mdto.getMem_idx();
				Long unixTime=System.currentTimeMillis();
				String lb_idx="LB"+unixTime;
				dto.setLb_idx(lb_idx);
				dto.setMem_idx(mem_idx);
				dto.setBook_idx(bk_idx);
				dto.setLb_return(1);
				System.out.println("메일보내기전");
				int result = bookDao.bookTakeUpdate(bk_idx);
				int sunbun = yeyakDao.yeyakUp(isbn);
				int set = loanDao.checkOutGo(dto);
				
				List<YeyakDTO> list = yeyakDao.yeyakList(isbn);
				if(list.size()!=0){
					System.out.println("메일보내기 위한 리스트 추출="+list);
					System.out.println(list.get(0).getMem_idx());
					String ye_mem="";
					for(int i=0; i<list.size(); i++){
						ye_mem = list.get(0).getMem_idx();
					}
					System.out.println("1순번 idx ="+ye_mem);
					MemberDTO memdto = memberDao.memberInfo(ye_mem);
					String ye_id = memdto.getMem_id();
					System.out.println("1순번 mail ="+ye_id);
					
					String content="예약하신 도서의 대출순번이 1순위가 되셨습니다. 2일안에 빌리지 못하시면 우선순위가 사라지십니다. 자세한 사항은 문의주세요";
					EmailDAO dao = new EmailDAO();
					dao.sendEmail(ye_id, content);
				}
				
				System.out.println("컨트롤러dto: " + dto);
				String msg = set>0?"도서 대출":"도서 대출 실패";
				mav = new ModelAndView("admin/adminMsg","msg",msg);
				mav.addObject("page","loanbookList.ju");
			}else{
				String msg = "이미 대출중인 도서입니다. 다른도서를 빌려주세요";
				mav = new ModelAndView("admin/adminMsg","msg",msg);
				mav.addObject("page","loanbookList.ju");
			}
			return mav;
		}
		
	// 예약 순번 빼기
		@RequestMapping("/sunbunDel.ju")
		public ModelAndView sunbunDel(
				@RequestParam(value="bk_isbn",defaultValue="0")String bk_isbn,
				@RequestParam(value="mem_idx",defaultValue="0")String mem_idx){
			int result = yeyakDao.yeyakDel(mem_idx);
			int count = yeyakDao.yeyakUp(bk_isbn);
			String msg = result>0?"예약순번 삭제":"예약순번 삭제";
			ModelAndView mav = new ModelAndView("admin/adminMsg","msg",msg);
			mav.addObject("page","yeyakList.ju");
			return mav;
		}
	
	// 반납하기
		@RequestMapping(value="/checkIn2.ju",method=RequestMethod.GET)
		public ModelAndView checkInStart(
				@RequestParam(value="bk_idx",defaultValue="0")String bk_idx,
				@RequestParam(value="bk_isbn",defaultValue="0")String bk_isbn,
				@RequestParam(value="lb_return",defaultValue="1")int lb_return){
			int result = bookDao.bookReUpdate(bk_idx);
			if(lb_return==2){
				int re = fedexDao.fedexDel(bk_idx);
				System.out.println("택배반납됨");
			}
			int count = loanDao.checkInGo(bk_idx);
			int yeyak = yeyakDao.yeyakOne(bk_isbn);
			String msg = "";
			if(yeyak!=0){
				msg = "예약이 된 도서입니다. 따로 보관 바랍니다.";
			}else{
				msg = count>0?"도서 반납":"도서 반납 실패";
			}
			ModelAndView mav = new ModelAndView("admin/adminMsg","msg",msg);
			mav.addObject("page","loanbookList.ju");
			return mav;
		}
		
	// 예약자 관리
		@RequestMapping("yeyakList.ju")
		public ModelAndView yeyakList(
				@RequestParam(value="bk_isbn",defaultValue="0")String bk_isbn){
			List<YeyakDTO> list = yeyakDao.yeyakList(bk_isbn);
			String dateFormat="yyyy-MM-dd";
			SimpleDateFormat sdf=new SimpleDateFormat(dateFormat);
			for(int i=0; i<list.size(); i++){
				String sdDay = sdf.format(list.get(i).getYe_date());
				list.get(i).setYe_day(sdDay);
			}
			ModelAndView mav = new ModelAndView("admin/loanbookManage/yeyakList","list",list);
			return mav;
		}
		
	// 메일보내기
		@RequestMapping("/mailSend.ju")
		public ModelAndView mailSend(
				@RequestParam(value="mem_id",defaultValue="0")String mem_id){

			String content="책 반납 하루 전입니다.";
	    	EmailDAO dao = new EmailDAO();
	    	dao.sendEmail(mem_id, content);
	    	String msg = "메일전송 완료";
	    	ModelAndView mav = new ModelAndView("admin/adminMsg","msg",msg);
	    	mav.addObject("page","loanbookList.ju");
			return mav;
		}
}