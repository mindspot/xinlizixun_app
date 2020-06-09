let workTypeContent = document.querySelectorAll('.main__work_type__content')[0];
let majorLabel = document.querySelectorAll('.main__major__content')[0];
let labelSectionItem1 = document.querySelectorAll('.main__label__type__section-1')[0];
let labelSectionItem2 = document.querySelectorAll('.main__label__type__section-2')[0];
let pointAtContent = document.querySelectorAll('.main__point_at__title__content')[0];
let footer = document.querySelector('#submit');
let work_type_content_id = 0;
let major = [];
let labelItem1 = [];
let labelItem2 = [];
let pointAt = [];
let increment = '';
function addEventClick(majorLabel, labelSectionItem1, labelSectionItem2, pointAtContent) {
    majorLabel.forEach(item => {
        let flag = true
        item.onclick = function() {
            let children = this.children[0];
            if(!major.includes(this.getAttribute('data-value'))) {
                if (major.length <= 2) {
                    flag = true;
                    major.push(this.getAttribute('data-value'));
                } else {
                    flag = false;
                }
            } else {
                major = major.filter(item => {
                    if (item === this.getAttribute('data-value')) {
                        return false;
                    }
                    return true;
                });
            }
            if (children.getAttribute('class').split(' ').length > 1) {
                children.removeAttribute('class', 'main__major__content__label__frame-selected');
                children.setAttribute('class', 'main__major__content__label__frame');
            } else {
                if (flag) {
                    children.setAttribute('class', 'main__major__content__label__frame-selected main__major__content__label__frame');
                }
            }
        }
    });
    
    for (let i = 0; i < labelSectionItem1.length; ++i) {
        let flag = true;
        item = labelSectionItem1[i];
        item.onclick = function() {
            if(!labelItem1.includes(this.getAttribute('data-value'))) {
                if (labelItem1.length <= 4) {
                    flag = true;
                    labelItem1.push(this.getAttribute('data-value'));
                } else {
                    flag = false;
                }
            } else {
                labelItem1 = labelItem1.filter(item => {
                    if (item === this.getAttribute('data-value')) {
                        return false;
                    }
                    return true;
                });
            }
            if (this.getAttribute('class').split(' ').length > 1) {
                this.removeAttribute('class', 'main__label__type__section__item-selected');
                this.setAttribute('class', 'main__label__type__section__item');
            } else {
                if (flag) {
                    this.setAttribute('class', 'main__label__type__section__item-selected main__label__type__section__item');
                }
            }
        }
    }
    
    for (let i = 0; i < labelSectionItem2.length; ++i) {
        let flag = true;
        item = labelSectionItem2[i];
        item.onclick = function() {
            if(!labelItem2.includes(this.getAttribute('data-value'))) {
                if (labelItem2.length <= 4) {
                    flag = true;
                    labelItem2.push(this.getAttribute('data-value'));
                } else {
                    flag = false;
                }
            } else {
                labelItem2 = labelItem2.filter(item => {
                    if (item === this.getAttribute('data-value')) {
                        return false;
                    }
                    return true;
                });
            }
            if (this.getAttribute('class').split(' ').length > 1) {
                this.removeAttribute('class', 'main__label__type__section__item-selected');
                this.setAttribute('class', 'main__label__type__section__item');
            } else {
                if (flag) {
                    this.setAttribute('class', 'main__label__type__section__item-selected main__label__type__section__item');
                }
            }
        }
    }
    
    for (let i = 0; i < pointAtContent.length; ++i) {
        item = pointAtContent[i];
        item.onclick = function() {
            let children = this.children[0];
            if(!pointAt.includes(this.getAttribute('data-value'))) {
                pointAt.push(this.getAttribute('data-value'));
            } else {
                pointAt.filter(item => {
                    if (item === this.getAttribute('data-value')) {
                        return false;
                    }
                    return true;
                });
            }
            if (children.getAttribute('class').split(' ').length > 1) {
                children.removeAttribute('class', 'main__major__content__label__frame-selected');
                children.setAttribute('class', 'main__major__content__label__frame');
            } else {
                children.setAttribute('class', 'main__major__content__label__frame-selected main__major__content__label__frame');
            }
        }
    }
}
function workHtml() {
    let work_type_content = [];
    let i = 1;
    result.result.work.forEach(item => {
        let content = `<div class="main__work_type__content__line">
        <span class="main__work_type__content__line__type main__work_type__content__common">
            ${item.workVal}
        </span>
        <span class="main__work_type__content__line__time main__work_type__content__common">
            ${item.workTime}
        </span>
        <input class="main__work_type__content__line__money main__work_type__content__common" value="">
        <span data-id="${item.id}" class="main__work_type__content__line__icon main__work_type__content__line__icon-${i === result.result.work.length ? 'increment' : 'reduce'} main__work_type__content__common"></span>
        </div>`
        i++;
        if (i === result.result.work.length + 1) {
            work_type_content_id = item.id;
        }
        work_type_content.push(content);
        work_type_content_add(work_type_content);
    });
}
function majorHtml() {
    let content = `<div class="main__major__content__line">`;
    let index = 1;
    result.result.classification.forEach(item => {
        content += `
        <span class="main__major__content__label" data-value="${item.id}">
            <span class="main__major__content__label__frame"></span>
            <span class="main__major__content__label__value">${item.val}</span>
        </span>`
        if (index % 3 === 0) {
            content += `<div class="line1"></div>`
        } else if (index % 3 !== 0 && index === result.result.classification.length) {
            content += `<div class="line1"></div>`
        }
        index++;
    });
    content += '</div>';
    major_content_add(content);
}
function labelSectionItem1Html() {
    let content = '';
    result.result.personalGrowth.forEach(item => {
		content += `<span class="main__label__type__section__item" data-value="${item.id}">${item.val}</span>`
    });
    labelSectionItem1.innerHTML = content;
}
function labelSectionItem2Html() {
    let content = '';
    result.result.emotionManagement.forEach(item => {
		content += `<span class="main__label__type__section__item" data-value="${item.id}">${item.val}</span>`
    });
    labelSectionItem2.innerHTML = content;
}
function pointAtContentHtml() {
    let content = '';
    result.result.contention.forEach(item => {
		content += `<span class="main__major__content__label main__point_at__title__section" data-value="${item.id}">
        <span class="main__major__content__label__frame"></span>
        <span class="main__major__content__label__value">${item.val}</span>
    </span>`
    });
    pointAtContent.innerHTML = content;
}
function work_type_content_add (work_type_content) {
    workTypeContent.innerHTML = '';
    work_type_content.forEach(item => {
        workTypeContent.innerHTML += item;
    })
}
function major_content_add (work_content) {
    majorLabel.innerHTML = work_content;
}
window.onload = function() {
    workHtml();
    majorHtml();
    labelSectionItem1Html();
    labelSectionItem2Html();
    pointAtContentHtml();
    workTypeContent.onclick = function(event) {
        if (event.target.getAttribute('class').includes('increment')) {
            result.result.work.push({
                "id": work_type_content_id + 1,
                "updateTime": 1583829028000,
                "insertTime": 1583829028000,
                "isDelete": 0,
                "workVal": "视频",
                "workTime": 50,
                "type": 1,
                "termOfValidity": 0,
                "workNumber": 0,
                "status": 1
            })
            
        }
        if (event.target.getAttribute('class').includes('reduce')) {
            let id = event.target.getAttribute('data-id');
            let index1 = 0;
            result.result.work.forEach((item, index) => {
                console.log(item.id, id);
                if (item.id == id) {
                    index1 = index;
                }
            });
            result.result.work.splice(index1, 1);
        }
        workHtml();
    }
    let majorLabel = document.querySelectorAll('.main__major__content')[0].querySelectorAll('.main__major__content__label');
    addEventClick(majorLabel, labelSectionItem1.children, labelSectionItem2.children, pointAtContent.children);
    footer.onclick = function() {
        console.log(major,labelItem1,labelItem2,pointAt);
    }
}
