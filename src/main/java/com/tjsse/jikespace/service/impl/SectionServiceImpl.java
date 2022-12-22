package com.tjsse.jikespace.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tjsse.jikespace.entity.CollectAndSection;
import com.tjsse.jikespace.entity.Section;
import com.tjsse.jikespace.entity.SectionAndSubSection;
import com.tjsse.jikespace.entity.SubSection;
import com.tjsse.jikespace.entity.dto.*;
import com.tjsse.jikespace.entity.vo.*;
import com.tjsse.jikespace.mapper.CollectAndSectionMapper;
import com.tjsse.jikespace.mapper.SectionAndSubSectionMapper;
import com.tjsse.jikespace.mapper.SectionMapper;
import com.tjsse.jikespace.mapper.SubSectionMapper;
import com.tjsse.jikespace.service.*;
import com.tjsse.jikespace.utils.Result;
import com.tjsse.jikespace.utils.JKCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * 版块服务的实现
 *
 * @author wlf 1557177832@qq.com
 * @version 2022/12/3 14:02
 * @since JDK18
 */
@Service
public class SectionServiceImpl implements SectionService {
    @Autowired
    private SectionMapper sectionMapper;
    @Autowired
    private SubSectionMapper subSectionMapper;
    @Autowired
    private SectionAndSubSectionMapper sectionAndSubSectionMapper;
    @Autowired
    private CollectAndSectionMapper collectAndSectionMapper;
    @Autowired
    private CollectService collectService;
    @Autowired
    private PostService postService;
    @Autowired
    private ThreadService threadService;
    @Override
    public Result getSectionData(Long userId,SectionDataDTO sectionDataDTO) {
        Long sectionId = sectionDataDTO.getSectionId();
        Integer curPage = sectionDataDTO.getCurPage();
        Integer limit = sectionDataDTO.getLimit();

        if(sectionId==null||curPage==null||limit==null)
            return Result.fail(JKCode.PARAMS_ERROR.getCode(), JKCode.PARAMS_ERROR.getMsg(),null);

        Section section = this.findSectionById(sectionId);
        if(section==null){
            return Result.fail(-1,"该版块不存在，参数有误",null);
        }
        SectionDataVO sectionDataVO = new SectionDataVO();
        sectionDataVO.setSectionName(section.getSectionName());
        sectionDataVO.setPostCounts(section.getPostCounts());
        if(userId!=null){
            sectionDataVO.setIsCollected(collectService.isUserCollectSection(userId,sectionId));
        }
        else{
            sectionDataVO.setIsCollected(false);
        }
        sectionDataVO.setSectionSummary(section.getSectionSummary());
        sectionDataVO.setSubSectionList(this.findSubSectionBySectionId(sectionId));
        sectionDataVO.setPostVOList(postService.findPostBySectionIdWithPage(sectionId,curPage,limit));

        return Result.success(20000,"okk",sectionDataVO);
    }

    @Override
    public Section findSectionById(Long sectionId) {
        LambdaQueryWrapper<Section> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Section::getId,sectionId);
        queryWrapper.last("limit 1");
        return this.sectionMapper.selectOne(queryWrapper);
    }

    @Override
    public SubSection findSubSectionById(Long subsectionId) {
        LambdaQueryWrapper<SubSection> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SubSection::getId,subsectionId);
        queryWrapper.last("limit 1");
        return this.subSectionMapper.selectOne(queryWrapper);
    }

    @Override
    public Result getPostsByTag(PostsWithTagDTO postsWithTagDTO) {
        Long sectionId = postsWithTagDTO.getSectionId();
        Long subsectionId = postsWithTagDTO.getSubsectionId();
        Integer curPage = postsWithTagDTO.getCurPage();
        Integer limit = postsWithTagDTO.getLimit();

        if(sectionId==null||subsectionId==null||curPage==null||limit==null)
            return Result.fail(JKCode.PARAMS_ERROR.getCode(), JKCode.PARAMS_ERROR.getMsg(),null);
        Section section = this.findSectionById(sectionId);
        if(section==null){
            return Result.fail(-1,"该版块不存在，参数有误",null);
        }

        List<PostDataVO> postList = postService.findPostBySectionIdAndSubSectionId(sectionId, subsectionId, curPage, limit);

        SectionPostsVO sectionPostsVO = new SectionPostsVO();
        sectionPostsVO.setPostDataVOList(postList);
        sectionPostsVO.setTotal(section.getPostCounts());

        return Result.success(20000,sectionPostsVO);
    }

    @Override
    public List<SubSection> findSubSectionBySectionId(Long sectionId) {
        List<SubSection> subSectionList = subSectionMapper.findSubSectionBySectionId(sectionId);
        return subSectionList;
    }

    @Override
    public Result collectSection(Integer userId) {
        LambdaQueryWrapper<CollectAndSection> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CollectAndSection::getUserId,userId);
        List<CollectAndSection> collectAndSections = collectAndSectionMapper.selectList(queryWrapper);

        if(collectAndSections.size()==0){
            List<CollectSectionVO> collectSectionVOS = new ArrayList<>();
            return Result.fail(20000,"okk",collectSectionVOS);
        }

        List<Long> sectionIdList = new ArrayList<>();
        for (CollectAndSection collectAndSection :
                collectAndSections) {
            sectionIdList.add(collectAndSection.getSectionId());
        }

        LambdaQueryWrapper<Section> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(Section::getIsDeleted,false);
        queryWrapper1.in(Section::getId,sectionIdList);
        List<Section> sections = sectionMapper.selectList(queryWrapper1);

        return Result.success(20000,"okk",copyList(sections));
    }

    @Override
    public Result hotSection(Integer i) {
        LambdaQueryWrapper<Section> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Section::getPostCounts);
        queryWrapper.last("limit "+ i);
        List<Section> sections = sectionMapper.selectList(queryWrapper);

        return Result.success(20000,"okk",copyList(sections));
    }

    @Override
    public Result searchSection(String content) {
        LambdaQueryWrapper<Section> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Section::getPostCounts);
        queryWrapper.like(Section::getSectionName,content);
        List<Section> sections = sectionMapper.selectList(queryWrapper);
        return Result.success(20000,"okk",copyList(sections));
    }

    @Override
    public void updateSectionByCollectCount(Long sectionId, boolean b) {
        Section section = this.findSectionById(sectionId);
        threadService.updateSectionByCollectCount(sectionMapper,section,b);
    }

    @Override
    public void updateSectionByPostCount(Long sectionId, boolean b) {
        Section section = this.findSectionById(sectionId);
        threadService.updateSectionByPostCount(sectionMapper,section,b);
    }

    @Override
    public Result getUserSections(Long userId) {
        LambdaQueryWrapper<Section> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Section::getAdminId,userId);
        queryWrapper.eq(Section::getIsDeleted,false);
        List<Section> sections = sectionMapper.selectList(queryWrapper);
        List<MySectionsVO> mySectionsVOList = copyToMySectionsVO(sections);
        Map<String,Object> map = new HashMap<>();
        map.put("sectionInfo",mySectionsVOList);
        return Result.success(map);
    }

    @Override
    public Result addSubSection(AddSubSectionDTO addSubSectionDTO) {
        String[] subsections = addSubSectionDTO.getSubsections();
        Long sectionId = addSubSectionDTO.getSectionId();
        if(subsections.length==0){
            return Result.fail(-1,"参数有误",null);
        }
        Boolean flag =false;
        for (String name :
                subsections) {
            if(this.isSubSectionLegal(sectionId,name)){
                SubSection subSection = new SubSection();
                subSection.setName(name);
                subSectionMapper.insert(subSection);
                SectionAndSubSection sectionAndSubSection = new SectionAndSubSection();
                sectionAndSubSection.setSectionId(sectionId);
                sectionAndSubSection.setSubsectionId(subSection.getId());
                sectionAndSubSectionMapper.insert(sectionAndSubSection);
            }
            else {
                flag = true;
            }
        }
        if(flag){
            return Result.success(20000,"重复名的版块未创建",null);
        }
        return Result.success(20000,"okk",null);
    }

    @Override
    public Result changeSectionIntro(Long userId, ChangeIntroDTO changeIntroDTO) {
        Long sectionId = changeIntroDTO.getSectionId();
        String sectionIntro = changeIntroDTO.getSectionIntro();
        Section section = this.findSectionById(sectionId);
        if(!Objects.equals(userId, section.getAdminId())){
            return Result.fail(-1,"没有权限",null);
        }
        section.setSectionSummary(sectionIntro);
        sectionMapper.updateById(section);
        return Result.success(20000,"okk",null);
    }

    private boolean isSubSectionLegal(Long sectionId, String name) {
        List<SubSection> subSectionList = findSubSectionBySectionId(sectionId);
        for (SubSection subsection :
                subSectionList) {
            if (Objects.equals(name, subsection.getName())){
                return false;
            }
        }
        return true;
    }

    @Override
    public Result createSection(Long userId, String sectionName, String image, String sectionIntro, String[] subsection) {
        LambdaQueryWrapper<Section> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Section::getSectionName,sectionName);
        queryWrapper.eq(Section::getIsDeleted,false);
        queryWrapper.last("limit 1");
        Section section = sectionMapper.selectOne(queryWrapper);
        if(section!=null){
            return Result.fail(-1,"论坛里已有该版块",null);
        }
        Section section1 = new Section();
        section1.setSectionName(sectionName);
        section1.setSectionAvatar(image);
        section1.setSectionSummary(sectionIntro);
        section1.setAdminId(userId);
        sectionMapper.insert(section1);
        for(int i=0;i<subsection.length;i++){
            AddSubSectionDTO addSubSectionDTO = new AddSubSectionDTO(section1.getId(),subsection);
            this.addSubSection(addSubSectionDTO);
        }
        return Result.success(20000,"okk",null);
    }

    @Override
    public Result deleteSubSection(Long userId, Integer subsectionId) {
        LambdaQueryWrapper<SectionAndSubSection> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SectionAndSubSection::getSubsectionId,subsectionId);
        queryWrapper.last("limit 1");
        SectionAndSubSection sectionAndSubSection = sectionAndSubSectionMapper.selectOne(queryWrapper);

        Section section = this.findSectionById(sectionAndSubSection.getSectionId());
        if(!Objects.equals(userId, section.getAdminId())){
            return Result.fail(-1,"没有权限",null);
        }
        sectionAndSubSectionMapper.deleteById(sectionAndSubSection);

        SubSection subSection = this.findSubSectionById(Long.valueOf(subsectionId));
        subSectionMapper.deleteById(subSection);
        return Result.success(20000,"okk",null);
    }

    @Override
    public Result renameSubSection(RenameSubSectionDTO renameSubSectionDTO) {
        Long userId = renameSubSectionDTO.getUserId();
        Long subsectionId = renameSubSectionDTO.getSubsectionId();
        String name = renameSubSectionDTO.getName();

        LambdaQueryWrapper<SectionAndSubSection> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SectionAndSubSection::getSubsectionId,subsectionId);
        queryWrapper.last("limit 1");
        SectionAndSubSection sectionAndSubSection = sectionAndSubSectionMapper.selectOne(queryWrapper);

        LambdaQueryWrapper<Section> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(Section::getId,sectionAndSubSection.getSectionId());
        queryWrapper.last("limit 1");
        Section section = sectionMapper.selectOne(queryWrapper1);

        if(!Objects.equals(userId, section.getAdminId())){
            return Result.fail(-1,"没有权限",null);
        }
        List<SubSection> subSections = this.findSubSectionBySectionId(section.getId());
        for (SubSection sub :
                subSections) {
            if(name==sub.getName()){
                return Result.fail(-1,"名字不能与该版块下其他子版块名重复",null);
            }
        }
        LambdaQueryWrapper<SubSection> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.eq(SubSection::getId,subsectionId);
        queryWrapper2.last("limit 1");
        SubSection subSection = subSectionMapper.selectOne(queryWrapper2);
        subSection.setName(name);
        subSectionMapper.updateById(subSection);

        return Result.success(20000,"okk",null);
    }

    @Override
    public Result changeSectionAvatar(Long userId, Long sectionId, String avatar) {
        Section section = this.findSectionById(sectionId);
        if(!Objects.equals(userId, section.getAdminId())){
            return Result.fail(-1,"没有权限",null);
        }
        else {
            section.setSectionAvatar(avatar);
            sectionMapper.updateById(section);
            return Result.success(20000,"okk",null);
        }
    }


    private List<MySectionsVO> copyToMySectionsVO(List<Section> sections) {
        List<MySectionsVO> mySectionsVOList = new ArrayList<>();
        for (Section section :
                sections) {
            mySectionsVOList.add(copyToMySection(section));
        }
        return mySectionsVOList;
    }

    private MySectionsVO copyToMySection(Section section) {
        MySectionsVO mySectionsVO = new MySectionsVO();
        mySectionsVO.setSectionId(section.getId());
        mySectionsVO.setSectionAvatar(section.getSectionAvatar());
        mySectionsVO.setSectionName(section.getSectionName());
        mySectionsVO.setUserCounts(section.getUserCounts());
        mySectionsVO.setPostCounts(section.getPostCounts());
        mySectionsVO.setSectionIntro(section.getSectionSummary());
        mySectionsVO.setSubSectionList(findSubSectionBySectionId(section.getId()));
        return mySectionsVO;
    }

    private List<CollectSectionVO> copyList(List<Section> sections) {
        List<CollectSectionVO> collectSectionVOS = new ArrayList<>();
        for (Section section:
                sections) {
            collectSectionVOS.add(copy(section));

        }
        return collectSectionVOS;
    }

    private CollectSectionVO copy(Section section) {
        CollectSectionVO collectSectionVO = new CollectSectionVO();
        collectSectionVO.setSectionId(section.getId());
        collectSectionVO.setName(section.getSectionName());
        collectSectionVO.setSummary(section.getSectionSummary());
        collectSectionVO.setAvatar(section.getSectionAvatar());
        return collectSectionVO;
    }
}
