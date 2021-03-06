package com.fifteen.webproject.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fifteen.webproject.bean.entity.Answer;
import com.fifteen.webproject.bean.entity.ExamQuestion;
import com.fifteen.webproject.bean.entity.Question;
import com.fifteen.webproject.bean.vo.QuestionVo;
import com.fifteen.webproject.mapper.AnswerMapper;
import com.fifteen.webproject.mapper.ExamQuestionMapper;
import com.fifteen.webproject.mapper.QuestionBankMapper;
import com.fifteen.webproject.mapper.QuestionMapper;
import com.fifteen.webproject.service.QuestionService;
import com.fifteen.webproject.utils.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.fifteen.webproject.utils.CommonUtils.setEqualsQueryWrapper;
import static com.fifteen.webproject.utils.CommonUtils.setLikeWrapper;

/**
 * @Author Fifteen
 * @Date 2022/5/9
 **/
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionService {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private AnswerMapper answerMapper;
    @Autowired
    private ExamQuestionMapper examQuestionMapper;
    @Autowired
    private QuestionBankMapper questionBankMapper;

    @Override
    public QuestionVo getQuestionVoById(Integer id) {
        Question question = questionMapper.selectById(id);
        Answer answer = answerMapper.selectOne(new QueryWrapper<Answer>().eq("question_id", id));
        QuestionVo questionVo = new QuestionVo();
        // 设置字段
        questionVo.setQuestionContent(question.getQuContent());
        questionVo.setAnalysis(question.getAnalysis());
        questionVo.setQuestionType(question.getQuType());
        questionVo.setQuestionLevel(question.getLevel());
        questionVo.setQuestionId(question.getId());
        if (question.getImage() != null && !Objects.equals(question.getImage(), "")) {
            questionVo.setImages(question.getImage().split(","));
        }
        questionVo.setCreatePerson(question.getCreatePerson());
        // 设置所属题库
        if (!Objects.equals(question.getQuBankId(), "")) {
            String[] bids = question.getQuBankId().split(",");
            Integer[] bankIds = new Integer[bids.length];
            for (int i = 0; i < bids.length; i++) {
                bankIds[i] = Integer.parseInt(bids[i]);
            }
            questionVo.setBankId(bankIds);
        }
        if (answer != null) {
            String[] allOption = answer.getAllOption().split(",");
            String[] imgs = answer.getImages().split(",");
            QuestionVo.Answer[] qa = new QuestionVo.Answer[allOption.length];
            if (question.getQuType() != 2) {
                for (int i = 0; i < allOption.length; i++) {
                    QuestionVo.Answer answer1 = new QuestionVo.Answer();
                    answer1.setId(i);
                    answer1.setAnswer(allOption[i]);
                    if (i <= imgs.length - 1 && !Objects.equals(imgs[i], ""))
                        answer1.setImages(new String[]{imgs[i]});
                    if (i == Integer.parseInt(answer.getTrueOption())) {
                        answer1.setIsTrue("true");
                        answer1.setAnalysis(answer.getAnalysis());
                    }
                    qa[i] = answer1;
                }
            } else {// 多选
                for (int i = 0; i < allOption.length; i++) {
                    QuestionVo.Answer answer1 = new QuestionVo.Answer();
                    answer1.setId(i);
                    answer1.setAnswer(allOption[i]);
                    answer1.setImages(imgs);
                    if (i < answer.getTrueOption().split(",").length && i == Integer.parseInt(answer.getTrueOption().split(",")[i])) {
                        answer1.setIsTrue("true");
                        answer1.setAnalysis(answer.getAnalysis());
                    }
                    qa[i] = answer1;
                }
            }
            questionVo.setAnswer(qa);
        }
        return questionVo;
    }

    @Override
    public List<QuestionVo> getAllQuestion(Integer examId) {
        ExamQuestion examQuestion = examQuestionMapper.selectOne(new QueryWrapper<ExamQuestion>().eq("exam_id", examId));
        String questionIds = examQuestion.getQuestionIds();
        String[] split = questionIds.split(",");
        ArrayList<QuestionVo> result = new ArrayList<>();
        for (String s : split) {
            int temp = Integer.parseInt(s);
            QuestionVo questionVoById = getQuestionVoById(temp);
            result.add(questionVoById);
        }
        return result;
    }

    @Override
    public Result<List<Question>> getQuestion(String questionType, String questionBank, String questionContent) {
        QueryWrapper<Question> wrapper = new QueryWrapper<>();
        HashMap<String, Object> map = new HashMap<>();
        map.put("qu_bank_name", questionBank);
        map.put("qu_content", questionContent);
        setLikeWrapper(wrapper, map);
        setEqualsQueryWrapper(wrapper, Collections.singletonMap("qu_type", questionType));
        List<Question> questions = questionMapper.selectList(wrapper);
        return new Result<>(questions, true, null);
    }

    @Override
    public void deleteQuestionByIds(String questionIds) {
        String[] ids = questionIds.split(",");
        HashMap<String, Object> map = new HashMap<>();
        for (String id : ids) {
            map.clear();
            map.put("question_id", id);
            //  1. 删除数据库的题目信息
            questionMapper.deleteById(Integer.parseInt(id));
            // 2. 删除答案表对应当前题目id的答案
            answerMapper.deleteByMap(map);
        }
    }

    @Override
    public void addQuestion(QuestionVo questionVo) {
        // 查询所有的问题,然后就可以设置当前问题的id了
        List<Question> qus = questionMapper.selectList(null);
        Integer currentQuId = qus.get(qus.size() - 1).getId() + 1;
        Question question = new Question();
        // 设置基础字段
        question.setQuType(questionVo.getQuestionType());
        question.setId(currentQuId);
        setQuestionField(question, questionVo);
        // 设置题目插图
        if (questionVo.getImages() != null) {
            String QuImages = Arrays.toString(questionVo.getImages());
            question.setImage(QuImages.substring(1, QuImages.length() - 1).replaceAll(" ", ""));
        }
        buildBankName(questionVo, question);

        questionMapper.insert(question);
        // 设置答案对象
        StringBuilder multipleChoice = new StringBuilder();
        if (questionVo.getQuestionType() != 4) {// 不为简答题
            Answer answer = new Answer();
            answer.setQuestionId(currentQuId);
            StringBuilder imgs = new StringBuilder();
            StringBuilder answers = new StringBuilder();
            for (int i = 0; i < questionVo.getAnswer().length; i++) {
                if (questionVo.getAnswer()[i].getImages()!=null) {// 如果该选项有一张图片信息
                    imgs.append(questionVo.getAnswer()[i].getImages()[0]).append(",");
                }
                buildAnswer(answers, questionVo, i, multipleChoice, answer);
            }
            buildMultiQuestionAnswer(questionVo, multipleChoice, answer, imgs, answers);
            answerMapper.insert(answer);
        }
    }

    @Override
    public void updateQuestion(QuestionVo questionVo) {
        Question question = new Question();
        // 设置基础字段
        question.setQuType(questionVo.getQuestionType());
        question.setId(questionVo.getQuestionId());
        setQuestionField(question, questionVo);
        // 设置题目插图
        if (questionVo.getImages() != null && questionVo.getImages().length != 0) {
            String QuImages = Arrays.toString(questionVo.getImages());
            question.setImage(QuImages.substring(1, QuImages.length() - 1).replaceAll(" ", ""));
        }
        buildBankName(questionVo, question);
        // 更新
        questionMapper.update(question, new UpdateWrapper<Question>().eq("id", questionVo.getQuestionId()));
        // 设置答案对象
        StringBuilder multipleChoice = new StringBuilder();
        if (questionVo.getQuestionType() != 4) {// 不为简答题
            Answer answer = new Answer();
            answer.setQuestionId(questionVo.getQuestionId());
            StringBuilder imgs = new StringBuilder();
            StringBuilder answers = new StringBuilder();
            for (int i = 0; i < questionVo.getAnswer().length; i++) {
                if (questionVo.getAnswer()[i].getImages() != null && questionVo.getAnswer()[i].getImages().length > 0) {// 如果该选项有一张图片信息
                    imgs.append(questionVo.getAnswer()[i].getImages()[0]).append(",");
                }
                buildAnswer(answers, questionVo, i, multipleChoice, answer);
            }
            buildMultiQuestionAnswer(questionVo, multipleChoice, answer, imgs, answers);
            answerMapper.update(answer, new UpdateWrapper<Answer>().eq("question_id", questionVo.getQuestionId()));
        }
    }

    @Override
    public List<Question> getAllBQuestion() {
        return questionMapper.selectList(new QueryWrapper<Question>().eq("qu_type",4));
    }

    private void buildMultiQuestionAnswer(QuestionVo questionVo, StringBuilder multipleChoice, Answer answer, StringBuilder imgs, StringBuilder answers) {
        if (questionVo.getQuestionType() == 2)
            answer.setTrueOption(multipleChoice.substring(0, multipleChoice.toString().length() - 1));
        String handleImgs = imgs.toString();
        String handleAnswers = answers.toString();
        if (handleImgs.length() != 0) handleImgs = handleImgs.substring(0, handleImgs.length() - 1);
        if (handleAnswers.length() != 0) handleAnswers = handleAnswers.substring(0, handleAnswers.length() - 1);

        // 设置答案的图片
        answer.setImages(handleImgs);
        // 设置所有的选项
        answer.setAllOption(handleAnswers);
    }

    private void buildAnswer(StringBuilder answers, QuestionVo questionVo, int i, StringBuilder multipleChoice, Answer answer) {
        answers.append(questionVo.getAnswer()[i].getAnswer()).append(",");
        // 设置对的选项的下标值
        if (questionVo.getQuestionType() == 2) {// 多选
            if (questionVo.getAnswer()[i].getIsTrue().equals("true")) multipleChoice.append(i).append(",");
        } else {// 单选和判断 都是仅有一个答案
            if (questionVo.getAnswer()[i].getIsTrue().equals("true")) {
                answer.setTrueOption(i + "");
                answer.setAnalysis(questionVo.getAnswer()[i].getAnalysis());
            }
        }
    }

    private void buildBankName(QuestionVo questionVo, Question question) {
        StringBuilder bankNames = new StringBuilder();
        for (Integer integer : questionVo.getBankId()) {
            bankNames.append(questionBankMapper.selectById(integer).getBankName()).append(",");
        }
        String names = bankNames.toString();
        names = names.substring(0, names.length() - 1);
        question.setQuBankName(names);
    }

    private void setQuestionField(Question question, QuestionVo questionVo) {
        question.setCreateTime(new Date());
        question.setLevel(questionVo.getQuestionLevel());
        question.setAnalysis(questionVo.getAnalysis());
        question.setQuContent(questionVo.getQuestionContent());
        question.setCreatePerson(questionVo.getCreatePerson());
        // 设置所属题库
        String bankIds = Arrays.toString(questionVo.getBankId());
        question.setQuBankId(bankIds.substring(1, bankIds.length() - 1).replaceAll(" ", ""));
    }

}
