window.addEventListener('DOMContentLoaded',function(){
    const qiw=document.querySelector('.ques-id-wrapper');
    const qib=document.querySelector('.ques-info-box');
    const ob=document.querySelector('.options-box');
    const pnb=document.querySelector('.pre-next-box');
    const btn=document.querySelector('.btn');
    const questype=['【单项选择】','【多项选择】','【判断题】','【简答题】'];
    const marks=['A.','B.','C.','D.'];
    const quesId=sessionStorage.getItem('quesId').split(',');
    let userans=new Array(quesId.length);
    let xhr=new XMLHttpRequest();
    let curques=Number(location.search.substring(5));
    let curchecked=-1;
    // console.log(location.search);
    // console.log(curques);
    xhr.open('get','http://106.12.147.187:8777/student/getQuestionById/'+quesId[curques]);
    xhr.timeout=2000;
    xhr.ontimeout=function(){
        alert('网络似乎不太好');
    }
    xhr.setRequestHeader('token',sessionStorage.getItem('token'));
    xhr.send();
    xhr.onreadystatechange=function(){
        if(xhr.readyState===4){
            data=JSON.parse(xhr.response)['result'];
            console.log(data);
            qib.children[0].children[0].innerHTML=curques;
            qib.children[1].innerHTML='&nbsp'+questype[data['questionType']-1]+'&nbsp';
            qib.children[2].innerHTML=data['questionContent'];
            if(data['questionType']===4){
                let ta=document.createElement('textarea');
                ta.cols='30';
                ta.rows='10';
                ob.children[0].appendChild(ta);
            }else{
                for(let i=0;i<data['answer'].length;i++){
                    let inp=document.createElement('input');
                    inp.name='ans';
                    inp.setAttribute('class','ans');
                    // inp.setAttribute();
                    if(data['questionType']===3){
                        inp.type='radio';
                    }else if(data['questionType']===2){
                        inp.type='checkbox';
                    }else{
                        inp.type='radio';
                    }
                    ob.children[i].appendChild(inp);
                    ob.children[i].innerHTML+='&nbsp&nbsp&nbsp'+marks[i]+'&nbsp'+data['answer'][i]['answer'];
                }
            }
        }
    }
    for(let i=0;i<quesId.length;i++){
        let div=document.createElement('div');
        div.setAttribute('class','id-box');
        div.setAttribute('quesId',quesId[i]);
        div.innerHTML=i+1;
        qiw.appendChild(div);
    }
    qiw.children[curques-1].style='background-color:#ffd550;';
    for(let i of ob.children){
        i.addEventListener('click',function(){
            if(this.children[0].type==='radio'){
                if(curchecked!=-1){
                    ob.children[curchecked].children[0].removeAttribute('checked');
                }
                this.children[0].setAttribute('checked',true);
                curchecked=this.getAttribute('ans-id')-1;
            }else if(this.children[0].type==='checkbox'){
                if(this.children[0].getAttribute('checked')){
                    this.children[0].removeAttribute('checked');
                }else{
                    this.children[0].setAttribute('checked','true');
                }
            }
        })
    }
    pnb.addEventListener('click',function(e){
        if(e.target.nodeName==='INPUT'){
            let t=null;
            if(e.target.value==='上一题'){
                console.log(curques-1);
                if(curques-1!=0){
                    t=curques-1;
                    // console.log('./quespage.html?cur='+t);
                    location.href='./quespage.html?cur='+t;
                }
            }else if(e.target.value==='下一题'){
                console.log(curques+1);
                if(curques+1<=quesId.length){
                    t=curques+1;
                    // console.log('./quespage.html?cur='+t);
                    location.href='./quespage.html?cur='+t;
                }
            }
        }
    })
    for(let i of qiw.children){
        i.addEventListener('click',function(){
            location.href='./quespage.html?cur='+this.innerHTML;
        })
    }
    btn.addEventListener('click',function(){
        location.href='./exampage.html';
    })
})