create table user_tag_merge
(   uid UInt64,
    gender String,
    agegroup String,
    favor String
)engine=MergeTree()
     order by (uid);

insert into user_tag_merge values(1,'M','90后','sm');
insert into user_tag_merge values(2,'M','70后','sj');
insert into user_tag_merge values(3,'M','90后','ms');
insert into user_tag_merge values(4,'F','80后','sj');
insert into user_tag_merge values(5,'F','90后','ms');

select tag.1,tag.2, groupArray(uid)
from (
        select uid,
        arrayJoin([('gender', gender),
            ('agegroup', agegroup),
            ('favor', favor)
            ]) tag
from user_tag_merge
    ) t group by tag;

select tag.1,tag.2, groupBitmapState(uid)
from (
         select uid,
                arrayJoin([('gender', gender),
                    ('agegroup', agegroup),
                    ('favor', favor)
                    ]) tag
         from user_tag_merge
         ) t group by tag;



