window.addEventListener('DOMContentLoaded',function(){
    const qiw=document.querySelector('.ques-id-wrapper');
    const eib=document.querySelector('.exam-info-box');
    const iw=document.querySelector('.inner-wrapper');
    let quesId=null;
    let xhr=new XMLHttpRequest();
    // console.log(location.search);
    xhr.open('get','http://106.12.147.187:8777/public/getExamInfoById'+location.search);
    xhr.setRequestHeader('token',sessionStorage.getItem('token'));
    xhr.timeout=2000;
    xhr.ontimeout=function(){
        alert('网络似乎不太好');
    }
    xhr.send();
    xhr.onreadystatechange=function(){
        if(xhr.readyState===4){
            console.log('request sended');
            let data=JSON.parse(xhr.response);
            const tags=['examDesc','startTime','examDuration','endTime','totalScore']
            const tagsch=['考试描述：','开始时间：','答题时长：','结束时间：','总分：'];
            sessionStorage.setItem('quesId',data['result']['questionIds']);
            quesId=data['result']['questionIds'].split(',');
            eib.children[0].innerHTML=data['result']['examName'];
            for(let i=0;i<tags.length;i++){
                let div=document.createElement('div');
                div.setAttribute('class','info');
                div.innerHTML='<span class="textl">'+tagsch[i]+'</span><span class="textr">'+data['result'][tags[i]]+'</span>';
                eib.children[1].children[0].appendChild(div);
            }
            for(let i=0;i<quesId.length;i++){
                let div=document.createElement('div');
                div.setAttribute('class','id-box');
                div.setAttribute('quesId',quesId[i]);
                div.innerHTML=i+1;
                qiw.appendChild(div);
            }
        }
    }
    iw.addEventListener('click',function(e){
        // console.log(e.target.nodeName);
        if(e.target.nodeName==='INPUT'){
            if(e.target.value==='开始考试'){
                location.href='./quespage.html?cur=1';
            }else{
                location.href='./exampage.html';
            }
        }
    })
})