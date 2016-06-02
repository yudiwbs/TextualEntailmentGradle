package edu.upi.cs.yudiwbs.textualentailment.babakempat;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * Created by yudiwbs on 24/05/2016.
 * berdasarkan hasil dep parsing Syntaxnet, tentukan subj dan obj
 *
 *
 *


 masih bug:
 ==> dalam quote mending digabung aja? "xxx" -> OBJQUOTE
 id:3
 H:`` Does A Tiger Have A Necktie `` was produced in London .
 T:Loraine besides participating in Broadway's Dreamgirls, also participated in the Off-Broadway production of "Does A Tiger Have A Necktie". In 1999, Loraine went to London, United Kingdom. There she participated in the production of "RENT" where she was cast as "Mimi" the understudy.
 subyek:tiger
 Pos subyek:88
 subyek:necktie
 Pos subyek:94
 obyek:london
 Pos obyek:120
 Root:Have
 Root Tebak:loraine
 Skor Subyek:1.0, Skor Obyek:1.0
 Skor Root:0.6464111973330244

 persen digabung lagi (hilangkn spasi)
 id kalimat:7
 80 % approve of Mr. Bush .
 T:Mrs. Bush's approval ratings have remained very high, above 80%,
 even as her husband's have recently dropped below 50%.
 Subyek
 subyek:80 %
 Pos subyek:-1
 Obyek
 obyek:Mr. Bush
 Pos obyek:-1

 complete skull (parsial? a complete)
 id kalimat:8
 A complete Dakosaurus was discovered by Diego Pol .
 T:Recent Dakosaurus research comes from a complete skull found in Argentina in 1996,
 studied by Diego Pol of Ohio State University, Zulma Gasparini of Argentinas National
 University of La Plata, and their colleagues.
 Subyek
 subyek:A complete Dakosaurus
 Pos subyek:-1
 Obyek
 obyek:Diego Pol
 Pos obyek:94

 parsial
 id kalimat:28
 Burns surgeons approve Dr Wood 's spray-on skin .
 T:Dr Wood led a courageous and committed team in the fight to save 28 patients suffering
 from between two and 92 per cent body burns, deadly infections and delayed shock.
 As well as receiving much praise from both her own patients and the media, she also
 attracted controversy among other burns surgeons due to the fact that spray-on skin
 had not yet been subjected to clinical trials.
 Subyek
 subyek:Burns surgeons
 Pos subyek:-1
 Obyek
 obyek:Dr Wood 's spray-on skin
 Pos obyek:-1

 susah:
 A pro-women amendment == an amendment to its electoral law that would allow women to vote ..
 id kalimat:9
 A pro-women amendment was rejected by the National Assembly of Kuwait .
 T:On May 17, 2005, the National Assembly of Kuwait passed, by a majority of 35 to 23
 (with 1 abstention), an amendment to its electoral law that would allow women to vote and
 to stand as parliamentary candidates.
 Subyek
 subyek:A pro-women amendment
 Pos subyek:-1
 Obyek
 obyek:the National Assembly of Kuwait
 Pos obyek:17
 obyek:Kuwait
 Pos obyek:42

 7 june == june 7
 id kalimat:15
 Alfredo Cristiani visits Mexico on June 7 .
 T:Cauhtemoc Cardenas said during a news conference on 7 June that the visit to Mexico
 by Salvadoran president Alfredo Cristiani is a visit by "a repressive ruler who oppresses
 a large sector of his people."
 Subyek
 subyek:Alfredo Cristiani
 Pos subyek:108
 Obyek
 obyek:Mexico
 Pos obyek:77
 obyek:June 7
 Pos obyek:-1



 singkatan nama
 Capt. Robert F. Scott  =  Capt. Scott
 id kalimat:30
 Capt. Scott reached Scott Island in December 1902 .
 T:Scott Island was discovered and landed upon in December 1902 by
 Captain William Colbeck commander of the Morning, relief ship
 for Capt. Robert F. Scott's expedition.
 Subyek
 subyek:Capt. Scott
 Pos subyek:-1
 Obyek
 obyek:Scott Island
 Pos obyek:0
 obyek:December 1902
 Pos obyek:47

 nama: neil amstrong = armstrong
 id kalimat:460
 Neil Armstrong was the first man who landed on the Moon .
 T:spacecraft commander apollo xi first manned lunar landing mission armstrong first man walk moon one small step man one giant leap mankind historic words man dream ages fulfilled
 T prepro:spacecraft commander apollo xi first manned lunar landing mission armstrong first man walk moon one small step man one giant leap mankind historic words man dream ages fulfilled
 subyek:neil armstrong
 Pos subyek:-1

 buang tanda baca
 id kalimat:35
 Clark is a relative of Jones ' .
 T:The car which crashed against the mail-box belonged to James Clark, 68, an acquaintance of James Jones' family.
 Subyek
 subyek:Clark
 Pos subyek:61
 Obyek
 obyek:Jones '
 Pos obyek:-1

 ada sinonim dalam objek
 id kalimat:38
 Cristiani is accused of the assassination of six Jesuits .
 T:He said that "there is evidence that Cristiani was involved in the murder of the six Jesuit priests" which occurred on 16 November in San Salvador.
 Subyek
 subyek:Cristiani
 Pos subyek:37
 Obyek
 obyek:the assassination of six Jesuits
 Pos obyek:-1
 obyek:six Jesuits
 Pos obyek:-1

 stopwords the
 id kalimat:42
 David Cameron works as the shadow education secretary .
 T:Parents also had to contribute "much more fully", while "coasting" schools would be tackled, Ms Kelly told MPs. But shadow education secretary David Cameron, who backed some ideas, said other parts were a "complete muddle".
 Subyek
 subyek:David Cameron
 Pos subyek:143
 Obyek
 obyek:the shadow education secretary
 Pos obyek:-1

 stopwords: a
 id kalimat:44
 Dean invented a revolver .
 T:James Kerr had been the foreman for the Deane, Adams and Deane gun factory. Robert Adams, one of the partners and inventor of the Adams revolver, was Kerr's cousin.
 Subyek
 subyek:Dean
 Pos subyek:40
 Obyek
 obyek:a revolver
 Pos obyek:-1


 EU budget = budget for the EU
 id kalimat:46
 Diplomats agree on EU budget .
 T:The BBC's Tim Franks says frustrated British diplomats insist there have been
 several achievements but there is no doubt that the continued delay in reaching
 agreement on a budget for the EU hangs over all discussions.
 Subyek
 subyek:Diplomats
 Pos subyek:-1
 Obyek
 obyek:EU budget
 Pos obyek:-1

 a treatment for burns victims = spray on skin for burns victims, a treatment

 id kalimat:47
 Dr Fiona Wood has invented a treatment for burns victims .
 T:She has become world renowned for her patented invention of spray on skin for burns victims,
 a treatment which is continually developing. Via her research, Fiona found that scarring is
 greatly reduced if replacement skin could be provided within 10 days. As a burns specialist
 the holy grail for Dr Fiona Wood is 'scarless, woundless healing'.

 Subyek
 subyek:Dr Fiona Wood
 Pos subyek:296
 Obyek
 obyek:a treatment for burns victims
 Pos obyek:-1
 obyek:burns victims
 Pos obyek:78

 variasi kalimat:
 the government of the United States = the governments of both the United States...
 the Civil War = the U.S. Civil War
 id kalimat:48
 During the Civil War the government of the United States bought arms from Britain .
 T:The British government did not initially purchase the weapon and civilian sales
 were modest. However the U.S. Civil War began in 1860 and the governments of both
 the United States and the Confederacy began purchasing arms in Britain.
 Subyek
 subyek:the government of the United States
 Pos subyek:-1
 Obyek
 obyek:the Civil War
 Pos obyek:-1
 obyek:the United States
 Pos obyek:162
 obyek:arms
 Pos obyek:217
 obyek:Britain
 Pos obyek:225

 terrorism = terrorist
 id kalimat:50
 El-Nashar is accused of terrorism .
 T:"I want to go back again. But I am afraid, honestly, I am afraid.
 Propaganda against me made people think I am terrorist.", said el-Nashar.
 Subyek
 subyek:El-Nashar
 Pos subyek:-1
 Obyek
 obyek:terrorism
 Pos obyek:-1

 Europe 's first pyramid = the first European pyramid
 id kalimat:54
 Europe 's first pyramid has been discovered near Sarajevo .
 T:Bosnia's leading Muslim daily Dnevni Avaz writes excitedly about
 "a sensational discovery" of "the first European pyramid" in the central
 town of Visoko, just north of Sarajevo.
 Subyek
 subyek:Europe 's first pyramid
 Pos subyek:-1
 Obyek
 obyek:Sarajevo
 Pos obyek:168


 a blackout in the capital = a blackout throughout most of the capital
 id kalimat:57
 FMLN caused a blackout in the capital .
 T:On the morning of 1 June, there was a blackout throughout most of the capital caused by urban commandos of the Farabundo Marti National Liberation Front (FMLN).
 T prepro:morning june blackout throughout capital caused urban commandos farabundo marti national liberation front fmln
 obyek:blackout capital
 Pos obyek:-1


 married = wife
 id kalimat:468
 The name of George H.W. Bush 's wife is Barbara .
 T:George Herbert Walker Bush (born June 12, 1924) is the former 41st President of the United States of America. Almost immediately upon his return from the war in December 1944, George Bush married Barbara Pierce.
 T prepro:george herbert walker bush born june 12 1924 former 41st president united states america almost immediately upon return war december 1944 george bush married barbara pierce
 subyek:name george bush wife
 Pos subyek:-1
 obyek:george bush wife
 Pos obyek:-1

 nama
 id kalimat:471
 Alfred Nobel is the inventor of dynamite .
 T:In 1867, Nobel obtained a patent on a special type of nitroglycerine, which he called "dynamite". The invention quickly proved its usefulness in building and construction in many countries.
 T prepro:1867 nobel obtained patent special type nitroglycerine called dynamite invention quickly proved usefulness building construction countries
 subyek:alfred nobel
 Pos subyek:-1

 tanggal june 1944 == june 6th 1944
 id kalimat:482
 The Normandy landings took place in June 1944 .
 T:The D-Day was the largest seaborne invasion force ever assembled headed for France on June 6th 1944.
 T prepro:day largest seaborne invasion force ever assembled headed france june 6th 1944
 subyek:normandy landings  <-- yang ini udah bener
 Pos subyek:-1
 obyek:place
 Pos obyek:-1
 obyek:june 1944
 Pos obyek:-1

 objek Christian Democratic Union tidak ketemu, salah parser
 id kalimat:483
 The name of Helmut Kohl 's political party is the Christian Democratic Union .
 T:Kohl participated in the late stage of WWII as a teenage soldier. He joined the Christian-Democratic Union (CDU) in 1947.
 T prepro:kohl participated late stage wwii teenage soldier joined christian democratic union cdu 1947
 subyek:name helmut kohl political party
 Pos subyek:-1
 obyek:helmut kohl political party
 Pos obyek:-1

==> hanya kena dr wood, tidak sprayskin
 Kalimat:dr wood led courageous committed team fight save 28 patients suffering two 92 per cent body burns deadly infections delayed shock well receiving much praise patients media also attracted controversy among burns surgeons due fact spray skin yet subjected clinical trials
 Yg dicari:dr wood spray skin
 max window awal:dr wood led courageous committed team
 max window setelah trim:dr wood
 max skor:0.5
 id kalimat:28
 Burns surgeons approve Dr Wood 's spray-on skin .
 T:Dr Wood led a courageous and committed team in the fight to save 28 patients suffering from between two and 92 per cent body burns, deadly infections and delayed shock. As well as receiving much praise from both her own patients and the media, she also attracted controversy among other burns surgeons due to the fact that spray-on skin had not yet been subjected to clinical trials.
 T prepro:dr wood led courageous committed team fight save 28 patients suffering two 92 per cent body burns deadly infections delayed shock well receiving much praise patients media also attracted controversy among burns surgeons due fact spray skin yet subjected clinical trials
 subyek:burns surgeons
 Pos subyek:205
 Obyek tebak:dr wood
 obyek:dr wood spray skin
 Pos obyek:-1

 ==> murder dengan kill
 Kalimat:said evidence cristiani involved murder six jesuit priests occurred 16 november san salvador
 Yg dicari:six jesuits
 max window awal:cristiani involved murder six
 max window setelah trim:involved six
 max skor:0.5

 id kalimat:40
 Cristiani killed six Jesuits .
 T:He said that "there is evidence that Cristiani was involved in the murder of the six Jesuit priests" which occurred on 16 November in San Salvador.
 T prepro:said evidence cristiani involved murder six jesuit priests occurred 16 november san salvador
 subyek:cristiani
 Pos subyek:14
 Obyek tebak:involved six
 obyek:six jesuits
 Pos obyek:-1

 ==> government dianggap beda dengan governments (dengan s). lematisasi?
 Kalimat:british government initially purchase weapon civilian sales modest however civil war began 1860 governments united states confederacy began purchasing arms britain
 Yg dicari:government united states
 max window awal:began 1860 governments united states
 max window setelah trim:1860 united states
 max skor:0.6666666666666666
 id kalimat:48
 During the Civil War the government of the United States bought arms from Britain .
 T:The British government did not initially purchase the weapon and civilian sales were modest. However the U.S. Civil War began in 1860 and the governments of both the United States and the Confederacy began purchasing arms in Britain.
 T prepro:british government initially purchase weapon civilian sales modest however civil war began 1860 governments united states confederacy began purchasing arms britain
 Subyek tebak:1860 united states
 subyek:government united states
 Pos subyek:-1
 obyek:civil war
 Pos obyek:75
 obyek:united states
 Pos obyek:108
 obyek:arms
 Pos obyek:151
 obyek:britain
 Pos obyek:156

 ==> european  europe (lematisasi)?
 Kalimat:bosnia leading muslim daily dnevni avaz writes excitedly sensational discovery first european pyramid central town visoko just north sarajevo
 Yg dicari:europe first pyramid
 max window awal:sensational discovery first european pyramid
 max window setelah trim:discovery first european pyramid
 max skor:0.6666666666666666
 id kalimat:54
 Europe 's first pyramid has been discovered near Sarajevo .
 T:Bosnia's leading Muslim daily Dnevni Avaz writes excitedly about "a sensational discovery" of "the first European pyramid" in the central town of Visoko, just north of Sarajevo.
 T prepro:bosnia leading muslim daily dnevni avaz writes excitedly sensational discovery first european pyramid central town visoko just north sarajevo
 Subyek tebak:discovery first european pyramid
 subyek:europe first pyramid
 Pos subyek:-1
 obyek:sarajevo
 Pos obyek:133


 => singkatan
 Kalimat:leading human rights group wednesday identified poland romania likely locations eastern europe secret prisons al qaeda suspects interrogated central intelligence agency
 Yg dicari:cia secret prisons
 max window awal:locations eastern europe secret prisons
 max window setelah trim:secret prisons
 max skor:0.6666666666666666

 id kalimat:34
 CIA secret prisons were located in Eastern Europe .
 T:A leading human rights group on Wednesday identified Poland and Romania as the likely locations in eastern Europe of secret prisons where al-Qaeda suspects are interrogated by the Central Intelligence Agency.
 T prepro:leading human rights group wednesday identified poland romania likely locations eastern europe secret prisons al qaeda suspects interrogated central intelligence agency
 Subyek tebak:secret prisons
 subyek:cia secret prisons
 Pos subyek:-1
 obyek:eastern europe
 Pos obyek:80

 ==> subyek tdk ketemu
 Glove cocok:galyean=galyeans(0.9999999795491235)
 Glove cocok:galyean=galyeans(0.9999999795491235)
 Glove cocok:galyean=galyeans(0.9999999795491235)
 Glove cocok:galyean=galyeans(0.9999999795491235)
 Glove cocok:galyean=galyeans(0.9999999795491235)
 Kalimat:john frances galyean partners life now share passion photography galyeans married 46 years involved photography since 1999
 Yg dicari:john galyean wife
 max window awal:john frances galyean partners life
 max window setelah trim:john frances galyean
 max skor:0.6666666666666666

 id kalimat:82
 John Galyean 's wife is called Frances .
 T:John and Frances Galyean are partners in life and now both share a passion for photography. The Galyeans, married for more than 46 years, have been involved in photography since 1999.
 T prepro:john frances galyean partners life now share passion photography galyeans married 46 years involved photography since 1999
 Subyek tebak:john frances galyean
 subyek:john galyean wife
 Pos subyek:-1

 ==>singkatan wwii = world war ii
 Kalimat:active member national guard called duty 1941 although kennon see active combat return home world war ii 1945
 Yg dicari:wwii
 max window awal:active member national
 max window setelah trim:
 max skor:0.0

 id kalimat:89
 Kennon did not participate in WWII .
 T:As an active member of the National Guard, he was called to duty in 1941.Although Kennon
 did not see active combat, he did not return home from World War II until May of 1945.
 T prepro:active member national guard called duty 1941 although kennon see active combat return home world war ii 1945
 subyek:kennon
 Pos subyek:55
 Obyek tebak:
 obyek:wwii
 Pos obyek:-1



 Glove cocok:italian=italy(0.8304322966818544)
 Glove cocok:italian=italy(0.8304322966818544)
 Glove cocok:italian=italy(0.8304322966818544)
 Glove cocok:italian=italy(0.8304322966818544)
 Pemotongan depan glove cocok:italian=italy(0.8304322966818544)
 Pemotongan belakang glove cocok:italian=italy(0.8304322966818544)
 Kalimat:way marlowe legally leave italy especially arrest warrant issued authorities assisted zaleshoff succeeds making escape milan
 Yg dicari:italian authorities
 max window awal:marlowe legally leave italy
 max window setelah trim:italy
 max skor:0.5

 ==> harusnya obyek tebak authorities?
 id kalimat:103
 Marlowe was arrested by Italian authorities .
 T:There is no way Marlowe could legally leave Italy, especially after an arrest warrant has been issued for him by the authorities. Assisted by Zaleshoff, he succeeds in making his escape from Milan.
 T prepro:way marlowe legally leave italy especially arrest warrant issued authorities assisted zaleshoff succeeds making escape milan
 subyek:marlowe
 Pos subyek:4
 Obyek tebak:italy
 obyek:italian authorities
 Pos obyek:-1

 ==> mercedes = mercedez (dengan z) skornya rendah
 glove cocok:benz=mercedes(0.8124374908328494)
 Pemotongan depan glove cocok:benz=mercedes(0.8124374908328494)
 Pemotongan belakang glove cocok:benz=mercedes(0.8124374908328494)
 Kalimat:since joining key cure campaign three years ago mercedes benz donated million toward finding new detection methods treatments cures women cancers
 Yg dicari:mercedez benz
 max window awal:three years ago mercedes
 max window setelah trim:mercedes
 max skor:0.5

 id kalimat:106
 Mercedez-Benz supports the Key to the Cure campaign .
 T:Since joining the Key to the Cure campaign three years ago, Mercedes-Benz has donated over $2 million toward finding new detection methods, treatments and cures for women's cancers.
 T prepro:since joining key cure campaign three years ago mercedes benz donated million toward finding new detection methods treatments cures women cancers
 Subyek tebak:mercedes
 subyek:mercedez benz
 Pos subyek:-1
 obyek:key
 Pos obyek:14
 obyek:cure campaign
 Pos obyek:18

 => konversi angka, ten  = 10
 Kalimat:auburn high school athletic hall fame recently introduced class 2005 includes 10 members
 Yg dicari:ten members
 max window awal:2005 includes 10 members
 max window setelah trim:members
 max skor:0.5
 id kalimat:155
 The Auburn High School Athletic Hall of Fame has ten members .
 T:The Auburn High School Athletic Hall of Fame recently introduced its Class of 2005 which includes 10 members.
 T prepro:auburn high school athletic hall fame recently introduced class 2005 includes 10 members
 subyek:auburn high school athletic hall fame
 Pos subyek:0
 obyek:fame
 Pos obyek:33
 Obyek tebak:members
 obyek:ten members
 Pos obyek:-1

 => lokasi, takes place = kick off
 Kalimat:fifth world social forum wsf kicked porto alegre rio grande sul state brazil
 Yg dicari:place
 max window awal:fifth world social
 max window setelah trim:
 max skor:0.0

 id kalimat:192
 The WSF takes place in Brazil .
 T:The fifth World Social Forum (WSF) has kicked off in Porto Alegre, Rio Grande do Sul state, Brazil.
 T prepro:fifth world social forum wsf kicked porto alegre rio grande sul state brazil
 subyek:wsf
 Pos subyek:25
 Obyek tebak:
 obyek:place
 Pos obyek:-1
 obyek:brazil
 Pos obyek:70

 => susah, masalah definisi "home". exiled italian royal return italy = exiled italian royal  return home
 Kalimat:decision allow exiled italian royal family return italy granted amid discovery head family prince vittorio emmanuele addressed president italy properly called president ciampi president president italians
 Yg dicari:home
 max window awal:decision allow exiled
 max window setelah trim:
 max skor:0.0
 id kalimat:202
 Italian royal family returns home .
 T:A decision to allow the exiled Italian royal family to return to Italy may be granted amid the discovery that the head of the family, Prince Vittorio Emmanuele, addressed the president of Italy properly. He has called President Ciampi "our president, the president of all Italians".
 T prepro:decision allow exiled italian royal family return italy granted amid discovery head family prince vittorio emmanuele addressed president italy properly called president ciampi president president italians
 subyek:italian royal family
 Pos subyek:22
 Obyek tebak:
 obyek:home
 Pos obyek:-1

 ==> dideteksi sosnovyi = yeonggwang  (padahal harusnya beda)
 Pemotongan depan glove cocok:sosnovyi=yeonggwang(0.9999999795491235)
 Pemotongan belakang glove cocok:sosnovyi=yeonggwang(0.9999999795491235)
 Kalimat:radiation leak nuclear power plant yeonggwang south jeolla province led officials yesterday shut facility
 Yg dicari:sosnovyi bor
 max window awal:nuclear power plant yeonggwang
 max window setelah trim:yeonggwang
 max skor:1.0
 id kalimat:206
 The nuclear power plant of Sosnovyi Bor suffers an emergency shut down .
 T:A radiation leak at a nuclear power plant in Yeonggwang, South Jeolla province, led officials yesterday to shut down the facility.
 T prepro:radiation leak nuclear power plant yeonggwang south jeolla province led officials yesterday shut facility
 Subyek tebak:nuclear power plant yeonggwang
 subyek:nuclear power plant sosnovyi bor
 Pos subyek:-1
 Subyek tebak:
 subyek:emergency
 Pos subyek:-1
 Obyek tebak:yeonggwang
 obyek:sosnovyi bor
 Pos obyek:-1


 Pemotongan belakang glove cocok:attacks=attack(0.700714551997573)
 Pemotongan belakang glove cocok:attacks=attack(0.700714551997573)
 Kalimat:suicide attack attack attacker attackers intend kill others intend die process see suicide suicide attack strict sense attacker dies attack example explosion crash caused attacker
 Yg dicari:tamil suicide attacks
 max window awal:suicide attack attack attacker attackers
 max window setelah trim:suicide attack attack
 max skor:0.6666666666666666

 ==> tamil penting sptnya lebih penting daripada term yang lain
 id kalimat:208
 People were killed in Tamil suicide attacks .
 T:A suicide attack is an attack in which the attacker or attackers intend to kill others and intend to die in the process (see suicide). In a suicide attack in the strict sense the attacker dies by the attack itself, for example in an explosion or crash caused by the attacker.
 T prepro:suicide attack attack attacker attackers intend kill others intend die process see suicide suicide attack strict sense attacker dies attack example explosion crash caused attacker
 Subyek tebak:
 subyek:people
 Pos subyek:-1
 Obyek tebak:suicide attack attack
 obyek:tamil suicide attacks
 Pos obyek:-1

 ==> tdk ketemu: Nuclear waste  = shipment radioactive waste
 Kalimat:anti nuclear protesters wednesday delayed progress shipment radioactive waste toward dump northern germany train stopped fourth time since crossing germany neared northern town lueneburg
 Yg dicari:nuclear waste transport
 max window awal:anti nuclear protesters wednesday delayed
 max window setelah trim:nuclear
 max skor:0.3333333333333333
 id kalimat:215
 Nuclear waste transport delayed in Germany .
 T:Anti-nuclear protesters on Wednesday delayed the progress of a shipment of radioactive waste toward a dump in northern Germany. The train stopped for the fourth time since crossing into Germany as it neared the northern town of Lueneburg.
 T prepro:anti nuclear protesters wednesday delayed progress shipment radioactive waste toward dump northern germany train stopped fourth time since crossing germany neared northern town lueneburg
 Subyek tebak:nuclear
 subyek:nuclear waste transport
 Pos subyek:-1
 obyek:germany
 Pos obyek:99

 ==> coref: the reform = spelling reform
 Kalimat:sides point german spelling need overhauling decades grown unwieldy reform approved german speaking countries 1996 make rules much easier cases new way harder
 Yg dicari:spelling reform
 max window awal:sides point german spelling
 max window setelah trim:spelling
 max skor:0.5
 id kalimat:225
 A spelling reform was approved in all German speaking countries .
 T:Both sides have a point. German spelling does need overhauling: over the decades, it has grown more unwieldy. But the reform, approved by German-speaking countries in 1996, does not make the rules much easier, and in some cases the new way is harder.
 T prepro:sides point german spelling need overhauling decades grown unwieldy reform approved german speaking countries 1996 make rules much easier cases new way harder
 Subyek tebak:spelling
 subyek:spelling reform
 Pos subyek:-1
 obyek:german speaking countries
 Pos obyek:84


 => susah, implisit outside ussr
 alimat:busby countered telling iconoclast point material chernobyl 800 miles east great britain traveled great britain contaminated wales scotland various parts united kingdom well said equally unfeasible travel distance opposite direction general flow wind examined computer models wind directions period gulf war quite clear material iraq come united kingdom particular types depressions anticyclone systems
 Yg dicari:chernobyl disaster
 max window awal:iconoclast point material chernobyl
 max window setelah trim:chernobyl
 max skor:0.5
 Kalimat:busby countered telling iconoclast point material chernobyl 800 miles east great britain traveled great britain contaminated wales scotland various parts united kingdom well said equally unfeasible travel distance opposite direction general flow wind examined computer models wind directions period gulf war quite clear material iraq come united kingdom particular types depressions anticyclone systems
 Yg dicari:repercussions outside ex ussr
 max window awal:busby countered telling iconoclast point material
 max window setelah trim:
 max skor:0.0
 id kalimat:227
 The Chernobyl disaster had repercussions outside the ex-USSR .
 T:Busby countered, telling The Iconoclast, the point is that material from Chernobyl which is 1,800 miles to the east of Great Britain traveled to Great Britain and contaminated Wales, Scotland, and various parts of the United Kingdom. And they might as well have said that it was equally unfeasible for it to travel that distance in the opposite direction to the general flow of the wind, but we have examined computer Models of wind directions over the period of the Gulf War and its quite clear the material from Iraq could have come through the United Kingdom because of the particular types of depressions and anticyclone systems that were there.
 T prepro:busby countered telling iconoclast point material chernobyl 800 miles east great britain traveled great britain contaminated wales scotland various parts united kingdom well said equally unfeasible travel distance opposite direction general flow wind examined computer models wind directions period gulf war quite clear material iraq come united kingdom particular types depressions anticyclone systems
 Subyek tebak:chernobyl
 subyek:chernobyl disaster
 Pos subyek:-1
 Obyek tebak:
 obyek:repercussions outside ex ussr
 Pos obyek:-1


 ==>ngga nemu: people republic of china = china
 love cocok:dalai=lama(0.7949665025841732)
 Glove cocok:dalai=lama(0.7949665025841732)
 Kalimat:china announcement rival panchen lama boy already recognized dalai lama indications communist regime desperate using means possible strengthen control tibet tibetan affairs
 Yg dicari:dalai lama government people republic china
 max window awal:china announcement rival panchen lama boy already recognized
 max window setelah trim:china announcement rival panchen lama
 max skor:0.5
 Kalimat:china announcement rival panchen lama boy already recognized dalai lama indications communist regime desperate using means possible strengthen control tibet tibetan affairs
 Yg dicari:people republic china
 max window awal:china announcement rival panchen lama
 max window setelah trim:china
 max skor:0.3333333333333333
 Kalimat:china announcement rival panchen lama boy already recognized dalai lama indications communist regime desperate using means possible strengthen control tibet tibetan affairs
 Yg dicari:dispute panchen lama reincarnation
 max window awal:china announcement rival panchen lama boy
 max window setelah trim:panchen lama
 max skor:0.5
 id kalimat:231
 Dalai Lama and the government of the People 's Republic of China are in dispute over Panchen Lama 's reincarnation .
 T:China's announcement of a rival Panchen Lama to the boy already recognized by the Dalai Lama are indications that its Communist regime is desperate in using any means possible to strengthen control over Tibet and Tibetan affairs.
 T prepro:china announcement rival panchen lama boy already recognized dalai lama indications communist regime desperate using means possible strengthen control tibet tibetan affairs
 Subyek tebak:china announcement rival panchen lama
 subyek:dalai lama government people republic china
 Pos subyek:-1
 Obyek tebak:china
 obyek:people republic china
 Pos obyek:-1
 Obyek tebak:panchen lama
 obyek:dispute panchen lama reincarnation
 Pos obyek:-1


 ==> coref he
 Glove cocok:claes=agusta(0.9999999795491235)
 Pemotongan depan glove cocok:claes=agusta(0.9999999795491235)
 Kalimat:claes denied categorically ever known existence agusta proposal told nato ambassadors
 know anything agusta payment socialist party
 Yg dicari:nato secretary general willy claes
 max window awal:ever known existence agusta proposal told nato
 max window setelah trim:agusta nato
 max skor:0.4
 id kalimat:234
 NATO Secretary General Willy Claes resigned .
 T:Claes denied categorically that they had ever known of the existence of an Agusta proposal, and told the NATO ambassadors that he did not know anything about an Agusta payment to the Socialist Party.
 T prepro:claes denied categorically ever known existence agusta proposal told nato ambassadors know anything agusta payment socialist party
 Subyek tebak:agusta nato
 subyek:nato secretary general willy claes
 Pos subyek:-1

 ==> nggak ketemu: cosmonot == russian
 Kalimat:cosmonaut sergei avdeyev currently board mir space station become person longest total stay space clocked 681 days
 Yg dicari:russians
 max window awal:cosmonaut sergei avdeyev
 max window setelah trim:
 max skor:0.0
 Kalimat:cosmonaut sergei avdeyev currently board mir space station become person longest total stay space clocked 681 days
 Yg dicari:record
 max window awal:cosmonaut sergei avdeyev
 max window setelah trim:
 max skor:0.0
 Kalimat:cosmonaut sergei avdeyev currently board mir space station become person longest total stay space clocked 681 days
 Yg dicari:longest stay space
 max window awal:person longest total stay space
 max window setelah trim:longest total stay space
 max skor:1.0
 id kalimat:239
 Russians hold record for longest stay in space .
 T:Cosmonaut Sergei Avdeyev, currently on board the Mir space station, has become the person with the longest total stay in space. He has clocked up 681 days.
 T prepro:cosmonaut sergei avdeyev currently board mir space station become person longest total stay space clocked 681 days
 Subyek tebak:
 subyek:russians
 Pos subyek:-1
 Obyek tebak:
 obyek:record
 Pos obyek:-1
 Obyek tebak:longest total stay space
 obyek:longest stay space
 Pos obyek:-1

 ==>nggak ketemu lady diana == Diana, Princess of Wales

 Kalimat:diana princess wales died sunday 31 august 1997 following car crash paris widespread public mourning death popular figure culminating funeral westminster abbey saturday september 1997
 Yg dicari:lady diana
 max window awal:diana princess wales died
 max window setelah trim:diana
 max skor:0.5
 id kalimat:243
 Lady Diana died in Paris .
 T:Diana, Princess of Wales died on Sunday, 31 August 1997 following a car crash in Paris. There was widespread public mourning at the death of this popular figure, culminating with her funeral at Westminster Abbey on Saturday, 6 September 1997.
 T prepro:diana princess wales died sunday 31 august 1997 following car crash paris widespread public mourning death popular figure culminating funeral westminster abbey saturday september 1997
 Subyek tebak:diana
 subyek:lady diana
 Pos subyek:-1
 obyek:paris
 Pos obyek:68

 =>coref new tidak ketemu epidemic=ebola

 Kalimat:ebola haemorrhagic fever fatal disease caused new virus known cure new epidemic detected zaire spring 1995 widely perceived threat west public attention intense
 Yg dicari:ebola epidemic
 max window awal:ebola haemorrhagic fever fatal
 max window setelah trim:ebola
 max skor:0.5
 id kalimat:246
 Ebola Epidemic breaks out in Zaire .
 T:Ebola haemorrhagic fever is a fatal disease caused by a new virus which has no known cure. When a new epidemic was detected in Zaire in the spring of 1995, it was widely perceived as a threat to the West. Public attention was intense.
 T prepro:ebola haemorrhagic fever fatal disease caused new virus known cure new epidemic detected zaire spring 1995 widely perceived threat west public attention intense
 Subyek tebak:ebola
 subyek:ebola epidemic
 Pos subyek:-1
 obyek:zaire
 Pos obyek:89

 ==> root salah:
 id:3
 H:`` Does A Tiger Have A Necktie `` was produced in London .
 T:Loraine besides participating in Broadway's Dreamgirls, also participated in the Off-Broadway production of "Does A Tiger Have A Necktie". In 1999, Loraine went to London, United Kingdom. There she participated in the production of "RENT" where she was cast as "Mimi" the understudy.
 Root:Have <--
 subyek:tiger
 Pos subyek:88
 subyek:necktie
 Pos subyek:94
 obyek:london
 Pos obyek:120

 ==> implisit
 H:Abuja is located in Adamawa State .
 T:I recently took a round trip from Abuja to Yola, the capital of Adamawa State
 and back to Abuja, with a fourteen-seater bus.
 subyek:abuja
 Pos subyek:25
 obyek:adamawa state
 Pos obyek:44
 Root:located
 Root Tebak:recently
 Skor Subyek:1.0, Skor Obyek:1.0
 Skor Root:0.268100878003545

 */

public class ProsesSubjObjSyntaxNet {

    private class HasilTebak {
        double nilai;   //nilai prediksi 0 sd 1. 1 artinya semakin mirip
        String tebakan; //yang paling mendkati
        public HasilTebak(double nilai, String tebakan) {
            this.nilai = nilai;
            this.tebakan = tebakan;
        }
    }

    /*
       contoh:

       id kalimat:4
        30 die in a bus collision in Uganda .
        T:A bus collision with a truck in Uganda has resulted in at least 30 fatalities and has left a further 21 injured.
        T prepro:bus collision truck uganda resulted least 30 fatalities left 21 injured
        obyek:bus collision uganda

        contoh:
        bus collision uganda == bus collision truck uganda  (perhatikan ada truck)
        output adalah: bus collision truck uganda   (mungkin posisi awal dan akhir?)

        contoh lain:
         T:On the morning of 1 June, there was a blackout throughout most of the capital caused by
            urban commandos of the Farabundo Marti National Liberation Front (FMLN).

         obyek: blackout in the capital

         output: a blackout throughout most of the capital

        cat: subkal dan kalimat sudah diprepro

        tambah lematisasi



        Skor Subyek:1.0, Skor Obyek:1.0



     */

    //ProsesLemma pl = new ProsesLemma();

    WordVectors vecGlove  = null;

    public ProsesSubjObjSyntaxNet () {
        String fileVecGlove = "D:\\eksperimen\\paragram\\paragram_300_sl999\\paragram_300_sl999\\paragram_300_sl999.txt";
        try {
            vecGlove = WordVectorSerializer.loadTxtVectors(new File(fileVecGlove));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public boolean isNumeric(String s) {
        return s.matches("[-+]?\\d*\\.?\\d+");
    }

    private double simKata(String s1, String s2) {
       double out = 0;
       //numeric tidak diproses
       if (!isNumeric(s1) && !isNumeric(s2)) {
           out = vecGlove.similarity(s1.trim(), s2.trim());
       }
       return out;
    }

    //mencari root hasil dep parser (biasanya verb) di dalam kalimat
    //setelah dicari dengak eksak tidak ketemu
    public HasilTebak tebakRoot(String kalimat, String root) {
        HasilTebak out=null;
        //parsing kalimat
        ArrayList<String> alKal = new ArrayList<>();
        Scanner scKal  = new Scanner(kalimat);
        while (scKal.hasNext()) {
            String kata = scKal.next();
            alKal.add(kata);
        }

        //root tidak diparsing karena hanya satu kata
        double maxSkor = 0;
        String maxKata = "";

        for (String sKata:alKal) {
            double skorSim = simKata(sKata,root);
            if ( skorSim > maxSkor ) {
                maxSkor = skorSim;
                maxKata = sKata;
            }
        }

        out = new HasilTebak(maxSkor,maxKata);
        return out;
    }


    //mencari subkal di dalam kalimat saat subkal tidak ada secara eksak di kalimat
    //memperhitungkan kata yg ditukar, sinonim
    public HasilTebak tebak(String kalimat,String subKal) {
        HasilTebak out;
        double batasCocok = 0.7;  //untuk glove
        int  tambahanLebarWindow = 2;
        //String out ="";
        //bergerak berdasarkan window, besarnya jumkata di subkal + 2

        //parsing subKal
        ArrayList<String> alSubkal = new ArrayList<>();
        Scanner scSubKal  = new Scanner(subKal);
        while (scSubKal.hasNext()) {
            String kata = scSubKal.next();
            alSubkal.add(kata);
        }

        //parsing kalimat
        ArrayList<String> alKal = new ArrayList<>();
        Scanner scKal  = new Scanner(kalimat);
        while (scKal.hasNext()) {
            String kata = scKal.next();
            alKal.add(kata);
        }

        int ukuranWindow = alSubkal.size()+tambahanLebarWindow;
        //System.out.println("ukuran window"+ukuranWindow);

        ArrayList<String> alWindow = new ArrayList<>();

        //loop untuk semua posisi window
        //  0  1  2 3 4 5 6 7 8 9
        // [0  1] 2
        //  0 [1  2]
        //  ...
        //                   [8 9]
        double  maxSkor=0;
        String maxWindow="";
        ArrayList<String> maxAlWindow = new ArrayList<>(); //isi window
        for (int i=0;i<=alKal.size()-ukuranWindow;i++) { //looop kata di kalimat

            //buat window berisi kata2
            alWindow.clear();
            StringBuilder sbWindow = new StringBuilder();
            for(int j=i; j<i+ukuranWindow ; j++) {
                alWindow.add(alKal.get(j));
                sbWindow.append(alKal.get(j));
                sbWindow.append(" ");
            }
            //System.out.println("window:"+sbWindow.toString());

            //hitung kemiripan isi window dengan subkalimat (alSubkal)
            int jumCocok = 0;
            for (String strSubKal:alSubkal) {  //loop semua string di subkal
                boolean isKetemu = false;
                for (String strWindow:alWindow) { //bandingkan dengan isi window
                    if (strSubKal.equals(strWindow)) { //ada yg persis
                       jumCocok++;
                       isKetemu = true;
                       break;
                    }
                }
                if (!isKetemu) {
                    //tidak ketemu yang persis di window
                    //pake glove
                    for (String strWindow:alWindow) { //ulangi lagi bandingkan dengan semua isi window
                        double skorSim = simKata(strSubKal,strWindow);
                        if (skorSim>=batasCocok) { // "mirip", anggap sama
                            //System.out.println("Glove cocok:"+strSubKal+"="+strWindow+"("+skorSim+")");
                            jumCocok++;
                            break; //berhenti pada yg pertama cocok
                        }
                    }
                }
            }

            //cari skor maks
            double skor = (double) jumCocok / alSubkal.size();
            if (skor>maxSkor) {
                maxSkor = skor;
                maxAlWindow.clear();
                maxAlWindow.addAll(alWindow);
                maxWindow = sbWindow.toString().trim();
            }
        } //end loop kata dalam kalimat


        //trim depan belakang untuk kata yg tidak ada di subkal
        //contoh: bus collision truck uganda resulted, bus collision uganda  =>
        //        bus collision truck uganda
        //resulted dibuang tidak ada di subkalimat

        //buang depan
        for (int i=0; i<maxAlWindow.size();i++) {
            boolean isKetemu = false;
            for (String strSubKal:alSubkal) {
                if (maxAlWindow.get(i).equals(strSubKal))  {
                    isKetemu = true;
                    break;
                }
            }


            if (!isKetemu) {
                //cek lagi dengan glove sim
                for (String strSubKal:alSubkal) {
                    double skorSim = simKata(maxAlWindow.get(i),strSubKal);
                    if (skorSim>=batasCocok) {
                        //System.out.println("Pemotongan depan glove cocok:"+strSubKal+"="+maxAlWindow.get(i)+"("+skorSim+")");
                        isKetemu = true;
                        break; //berhenti pada yg pertama cocok
                    }
                }
                if (!isKetemu) {  //tetap tidak ketemu, buang
                    maxAlWindow.remove(i);
                    i--; //mundurkan satu, karena proses remove menggeser
                }
            } else { //ada, ketemu batas, stop
               break;
            }
        }

        //buang belakang
        for (int i=maxAlWindow.size()-1; i>=0;i--) {
            boolean isKetemu = false;
            for (String strSubKal:alSubkal) {
                if (maxAlWindow.get(i).equals(strSubKal))  {
                    isKetemu = true;
                    break;
                }
            }
            if (!isKetemu) {
                //cek lagi dengan glove sim
                for (String strSubKal:alSubkal) {
                    double skorSim = simKata(maxAlWindow.get(i),strSubKal);
                    if (skorSim>=batasCocok) {
                       // System.out.println("Pemotongan belakang glove cocok:"+strSubKal+"="+maxAlWindow.get(i)+"("+skorSim+")");
                        isKetemu = true;
                        break; //berhenti pada yg pertama cocok
                    }
                }
                //tidak ada di subkal, hapus saja
                if (!isKetemu) {
                    maxAlWindow.remove(i);
                }
            } else { //ada, ketemu batas, stop
                break;
            }
        }

        StringBuilder sb = new StringBuilder();
        for (String s:maxAlWindow) {
            sb.append(s);
            sb.append(" ");
        }
        /*
        System.out.println("Kalimat:"+kalimat); //nanti ada batas?
        System.out.println("Yg dicari:"+subKal); //nanti ada batas?
        System.out.println("max window awal:"+maxWindow);
        maxWindow = sb.toString().trim();
        System.out.println("max window setelah trim:"+maxWindow);
        System.out.println("max skor:"+maxSkor); //nanti ada batas?
        */

        out = new HasilTebak(maxSkor,maxWindow);
        return out;
    }

    public void proses() {
        //load T mentah (tdk displit)

        // data train
        String fileT        = "D:\\desertasi\\eksperimen\\t_train.txt";
        String fileSynNet   = "D:\\desertasi\\eksperimen\\out_ver4_h.txt";

        //data test
        //String fileT       = "D:\\desertasi\\eksperimen\\t_test.txt";
        //String fileSynNet  = "D:\\desertasi\\eksperimen\\out_test_h.txt";


        ArrayList<String> alT = new ArrayList<>();

        Prepro pp = new Prepro();
        pp.loadStopWords("stopwords2","kata");



        try {
            Scanner sc = new Scanner(new File(fileT));
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                alT.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ParsingSyntaxNet psn = new ParsingSyntaxNet();

        //load dependency struktur untuk H
        ArrayList<SentenceDepTree> alSentence = psn.load(fileSynNet);

        //System.out.println("Jumlah kal:"+alSentence.size());

        int cc=0;
        //loop untuk H, bisa karena semua H terdiri atas satu sentnce
        for (SentenceDepTree sentence: alSentence) {
            //System.out.println("id kalimat:"+cc);
            //System.out.println(sentence.getKalimatAsli()); //h
            //String subyek="";
            //String obyek="";

            //bisa lebih dari satu, misal suby pasif (atau dipisah?)
            ArrayList<String> alSubyek = new ArrayList<>();
            ArrayList<String> alObyek = new ArrayList<>();
            String strRoot="";

            //loop semua dep H dalam satu kalimat
            for (DataDepTree d: sentence.alDataDepTree) {
                //ambil subyek
                if (d.rel.equals("nsubj")||d.rel.equals("nsubjpass")) {
                    ArrayList<DataDepTree> alData = new ArrayList<>();
                    alData.add(d);
                    //cari semua childnya kalau ada
                    ArrayList<DataDepTree> alChild = sentence.getChild(d.id);
                    alData.addAll(alChild);
                    //susun ulang berdasarikan id
                    alData.sort((o1, o2) -> o1.idInt - o2.idInt );
                    StringBuilder sb = new StringBuilder();
                    for (DataDepTree dS: alData) {
                        //System.out.println(dS.kata);
                        sb.append(dS.kata);
                        sb.append(" ");
                    }
                    String subyek = sb.toString().trim();

                    //buang stopwords dan selain alpha numeric
                    subyek = pp.loadKataTanpaStopWordstoString(subyek,true,true);

                    //lematisasi, bermasalah
                    //subyek  = pl.lemmatize(subyek);

                    //cek apakah subyek sudah ada sebelumnya, contoh
                    //sudah ada obyek ini: assassination six jesuits
                    //lalu masuk lagi obyek: six jesuits
                    boolean isKetemu = false;
                    for (String s: alSubyek) {
                        if (s.contains(subyek)) {
                            isKetemu = true;
                            break;
                        }
                    }

                    if (!isKetemu) {
                        alSubyek.add(subyek);
                    }
                    //System.out.println("Subyek:"+subyek);
                } else
                    //proses obyek
                    //masih copy paste, nanti dirapikan
                    if (d.rel.equals("iobj")||d.rel.equals("dobj")||d.rel.equals("pobj")) {
                        ArrayList<DataDepTree> alData = new ArrayList<>();
                        alData.add(d);
                        //cari semua childnya kalau ada
                        ArrayList<DataDepTree> alChild = sentence.getChild(d.id);
                        alData.addAll(alChild);
                        //susun ulang berdasarikan id
                        alData.sort((o1, o2) -> o1.idInt - o2.idInt );
                        StringBuilder sb = new StringBuilder();
                        for (DataDepTree dS: alData) {
                            //System.out.println(dS.kata);
                            sb.append(dS.kata);
                            sb.append(" ");
                        }
                        String obyek = sb.toString().trim();
                        //proses stopwords
                        obyek = pp.loadKataTanpaStopWordstoString(obyek,true,true);
                        //lematisasi
                        //obyek = pl.lemmatize(obyek);

                        boolean isKetemu = false;
                        for (String s: alObyek) {
                            if (s.contains(obyek)) {
                                isKetemu = true;
                                break;
                            }
                        }

                        if (!isKetemu) {
                            alObyek.add(obyek);
                        }
                        //System.out.println("Obyek:"+obyek);
                    } else //if obj
                    if (d.rel.equals("ROOT")) {  //root harus huruf besar
                        strRoot  = d.kata;

                    }
            } // for (DataDepTree d: sentence.alDataDepTree) dalam satu sentence

            StringBuilder sbOut = new StringBuilder();


            //obj dan subj H didapat, cari pasangannya untuk T
            //data T disimpan di variabel alT
            String t = alT.get(cc);
            String tPrepro = pp.loadKataTanpaStopWordstoString(t,true,true);

            //lematisasi, ... ternyata lebih banyak buat masalah, disable dulu
            //tPrepro = pl.lemmatize(tPrepro);

            /*
            System.out.println("id kalimat:"+cc);
            System.out.println(sentence.getKalimatAsli()); //h
            System.out.println("T:"+t);
            System.out.println("T prepro:"+tPrepro);
            */
            //di proses stopword

            double jumSubyekCocok  = 0;
            double jumObyekCocok   = 0;

            //proses pencarian objek dan subyek di T
            //System.out.println("Subyek");
            boolean adaYgTdkKetemu = false;


            //loop untuk semua subyek di H
            for (String subyek: alSubyek) {
                //cari apakah ada di T
                int posSubj = tPrepro.indexOf(subyek);
                //debug, hanya tampilkan yang tidak ketemu
                if (posSubj==-1) {  //tidak ketemu, gunakan perkiraan
                    //System.out.println("subyek:"+subyek);
                    //System.out.println("Pos subyek:"+posSubj);
                    adaYgTdkKetemu = true;

                    //coba ditebak

                    HasilTebak ht = tebak(tPrepro,subyek);

                    sbOut.append("Subyek tebak:"+ht.tebakan);
                    sbOut.append("Skor tebak:"+ht.nilai);
                    sbOut.append(System.lineSeparator());
                    jumSubyekCocok = jumSubyekCocok + ht.nilai;
                } else {
                    //ketemu
                    jumSubyekCocok = jumSubyekCocok + 1.0;

                }
                sbOut.append("subyek:"+subyek);
                sbOut.append(System.lineSeparator());
                sbOut.append("Pos subyek:"+posSubj);
                sbOut.append(System.lineSeparator());
            }

            //hitung kemiripan subyek
            double skorSubyek =  (double) jumSubyekCocok / alSubyek.size();

            //System.out.println("Obyek");
            //cari untuk obyek
            for (String obyek: alObyek) {
                int posObj  = tPrepro.indexOf(obyek);
                if (posObj==-1) {
                    //System.out.println("obyek:"+obyek);
                    //System.out.println("Pos obyek:"+posObj);
                    adaYgTdkKetemu = true;
                    //proses tebakan
                    //prosesTebak;
                    HasilTebak  ht = tebak(tPrepro,obyek);
                    sbOut.append("Obyek tebak:"+ht.tebakan);
                    sbOut.append(System.lineSeparator());
                    sbOut.append("Skor obyek tebak:"+ht.tebakan);
                    jumObyekCocok = jumObyekCocok + ht.nilai;
                    sbOut.append(System.lineSeparator());
                } else {
                    jumObyekCocok = jumObyekCocok + 1.0;
                }
                sbOut.append("obyek:"+obyek);
                sbOut.append(System.lineSeparator());
                sbOut.append("Pos obyek:"+posObj);
                sbOut.append(System.lineSeparator());
            }

            //hitung kemiripan subyek
            double skorObyek =  (double) jumObyekCocok / alObyek.size();
            double skorRoot;


            //cari root di T, nanti dicek diantara Subj dan Obj?
            //untuk sekarang coba cari saja semua, nanti bandingkan
            int posRoot  = tPrepro.indexOf(strRoot);
            sbOut.append("Root:"+strRoot);
            sbOut.append(System.lineSeparator());
            if (posRoot==-1) {
                //tidak ketemu, lalukan prediksi dengan glove
                HasilTebak  htRoot = tebakRoot(tPrepro,strRoot);
                skorRoot = htRoot.nilai;
                sbOut.append("Root Tebak:"+htRoot.tebakan);
                sbOut.append(System.lineSeparator());
            } else {
                skorRoot = 1;
            }
            //debug


            System.out.println("id:"+(cc+1));
            System.out.println("H:"+sentence.getKalimatAsli()); //h
            System.out.println("T:"+t);
            System.out.println(sbOut.toString());
            System.out.println("Skor Subyek:"+skorSubyek+", Skor Obyek:"+skorObyek);
            System.out.println("Skor Root:"+skorRoot);

            //end debug

            //untuk input weka
            /*
            String strSkorSubyek;
            if (Double.isNaN(skorSubyek)) {
                strSkorSubyek = "?";
            } else {
                strSkorSubyek = Double.toString(skorSubyek);
            }

            String strSkorObyek;
            if (Double.isNaN(skorObyek)) {
                strSkorObyek = "?";
            } else {
                strSkorObyek = Double.toString(skorObyek);
            }

            System.out.println(strSkorSubyek+","+strSkorObyek+","+skorRoot);
            /*
             //end input ke weka

            //debug: print hanya jika subyek atau obyek tidak ketemu
            /*
            if (adaYgTdkKetemu) {
                System.out.println("");
                System.out.println("id kalimat:"+cc);
                System.out.println(sentence.getKalimatAsli()); //h
                System.out.println("T:"+t);
                System.out.println("T prepro:"+tPrepro);
                System.out.println(sbOut.toString());
            }
            */
            cc++;
        }
    }

    public static void main(String[] args) {
        ProsesSubjObjSyntaxNet ps = new ProsesSubjObjSyntaxNet();
        //ps.proses();
        //ps.tebakRoot("cost","sold");
        //ps.tebak("morning june blackout throughout capital caused urban commandos farabundo marti national " +
        //        "liberation front fmln","blackout throughout capital ");
        //ps.tebak("collision truck uganda resulted least 30 fatalities left 21 injured",
        //         "bus collision uganda");
        //ps.tebak("collision truck uganda resulted least 30 fatalities left 21 injured",
        //         "bus collision uganda");
    }

}
