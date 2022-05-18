window.addEventListener('DOMContentLoaded',function(){
    const excUl=document.querySelector('.exc-ul');
    const ite=document.querySelector('.initpage');
    const nec=document.querySelector('.new-exc');
    const oec=document.querySelector('.old-exc');
    const btnexc=document.querySelectorAll('.btn');
    const sub=document.querySelectorAll('.subject');
    const pcs=document.querySelector('.plan-conf-sheet');
    const sec=document.querySelector('.sub-content');
    excUl.addEventListener('click',function(e){
        if(e.target.nodeName==='SPAN'){
            for(let i=0;i<this.children.length;i++){
                this.children[i].children[0].style='color:black; background-color:none;'
            }
            e.target.style='color:#ffffff; background-color:#1c80f2;';
            // console.dir(e.target);
            if(e.target.getAttribute('span-id')==='1'){
                oec.style='display:none;';
                nec.style='display:flex;';
                btnexc[0].style='display:block;';
                flag=1;
            }else{
                oec.style='display:flex;';
                nec.style='display:none;';
                btnexc[0].style='display:none;';
                btnexc[1].style='display:none;';
                flag=2;
            }
        }
    })
    // 科目选择交互
    for(let i of sub){
        i.addEventListener('click',function(){
            if(this.getAttribute('selected')===null){
                this.setAttribute('selected','1');
                this.children[0].style='display:block;';
            }else{
                this.removeAttribute('selected');
                this.children[0].style='display:none;';
            }
        })
    }
    for(let i of btnexc){
        i.addEventListener('click',function(){
            if(this.getAttribute('btn-id')==='0'){
                pcs.style="display:block";
                sec.style="display:none";
                this.innerHTML="完成";
                this.nextElementSibling.style="display:block;";
            }else{
                pcs.style="display:none";
                sec.style="display:flex";
                if(this.previousElementSibling.innerHTML==='完成'){
                    this.style="display:none;";
                    this.previousElementSibling.innerHTML='下一步';
                }
            }
        })      
    } 
})