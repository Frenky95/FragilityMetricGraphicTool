library(dplyr)
library(ggplot2)
library(stringr)
library(gsubfn)
library(Hmisc)
library(broom)
library(ggpubr)
dir.root <- "C:/Users/frenk/Documents/R-test data"
dir.origine <- paste(dir.root, "sing", sep="/")
file1 <- "ScannerPythonTesi.csv"
file2 <- "ScannerPythonTesi2.csv"
nomefile1.completo=paste(dir.origine, file1, sep="/")
nomefile2.completo=paste(dir.origine, file2, sep="/")

df <- read.csv(nomefile1.completo)
 
# CHANGE
change_pos = subset(df, CHANGE!="")
change_temp = (change_pos$CHANGE)
only_numbers = strapply(change_temp, "[(](\\d+)[)]", simplify=TRUE)
sum_change = 0
for (i in only_numbers)
  for (j in i)
    sum_change = sum_change + as.integer(j)
if (is.nan(sum_change))
  sum_change = 0.0
change_value = sum_change


df2 <- read.csv(nomefile2.completo)

# MRR
mrr = (df2$MRR)
if (is.nan(mrr))
  mrr = 0.0
mrr_value = mrr

# TSV
tsv = (df2$TSV)
if (is.nan(tsv))
  tsv = 0.0
tsv_value = tsv

# NTR
ntr = (df2$NTR)
if (is.nan(ntr))
  ntr = 0.0
ntr_value = ntr


ntc_plot <- ggplot(data=df, aes(x=VERS, y=NTC, group=1)) +
    geom_line()+ geom_point() + geom_line(aes(y = mean(NTC)), color = "red", linetype = "solid") + theme(axis.text.x=element_blank())

ttl_plot <- ggplot(data=df, aes(x=VERS, y=TTL, group=1)) +
  geom_line()+ geom_point() + geom_line(aes(y = mean(TTL)), color = "red", linetype = "solid") + theme(axis.text.x=element_blank())

tlr_plot <- ggplot(data=df, aes(x=VERS, y=TLR, group=1)) +
  geom_line()+ geom_point() + geom_line(aes(y = mean(TLR)), color = "red", linetype = "solid") + theme(axis.text.x=element_blank())

mtlr_plot <- ggplot(data=df, aes(x=VERS, y=MTLR, group=1)) +
  geom_line()+ geom_point() + geom_line(aes(y = mean(MTLR)), color = "red", linetype = "solid") + theme(axis.text.x=element_blank())

mrtl_plot <- ggplot(data=df, aes(x=VERS, y=MRTL, group=1)) +
  geom_line()+ geom_point() + geom_line(aes(y = mean(MRTL)), color = "red", linetype = "solid") + theme(axis.text.x=element_blank())

mcr_plot <- ggplot(data=df, aes(x=VERS, y=MCR, group=1)) +
  geom_line()+ geom_point() + geom_line(aes(y = mean(MCR)), color = "red", linetype = "solid") + theme(axis.text.x=element_blank())

mmr_plot <- ggplot(data=df, aes(x=VERS, y=MMR, group=1)) +
  geom_line()+ geom_point() + geom_line(aes(y = mean(MMR)), color = "red", linetype = "solid") + theme(axis.text.x=element_blank())

mcmmr_plot <- ggplot(data=df, aes(x=VERS, y=MCMMR, group=1)) +
  geom_line()+ geom_point() + geom_line(aes(y = mean(MCMMR)), color = "red", linetype = "solid") + theme(axis.text.x=element_blank())

cc_plot <- ggplot(data=df, aes(x=VERS, y=CC, group=1)) +
  geom_line()+ geom_point() + geom_line(aes(y = mean(CC)), color = "red", linetype = "solid") + theme(axis.text.x=element_blank())

codesmells_plot <- ggplot(data=df, aes(x=VERS, y=CODE_SMELLS, group=1)) +
  geom_line()+ geom_point() + geom_line(aes(y = mean(CODE_SMELLS)), color = "red", linetype = "solid") + theme(axis.text.x=element_blank())

td_plot <- ggplot(data=df, aes(x=VERS, y=TD, group=1)) +
  geom_line()+ geom_point() + geom_line(aes(y = mean(TD)), color = "red", linetype = "solid") + theme(axis.text.x=element_blank())

debtratio_plot <- ggplot(data=df, aes(x=VERS, y=DEBT_RATIO, group=1)) +
  geom_line()+ geom_point() + geom_line(aes(y = mean(DEBT_RATIO)), color = "red", linetype = "solid") + theme(axis.text.x=element_blank())

cloc_plot <- ggplot(data=df, aes(x=VERS, y=CLOC, group=1)) +
  geom_line()+ geom_point() + geom_line(aes(y = mean(CLOC)), color = "red", linetype = "solid") + theme(axis.text.x=element_blank())

loc_plot <- ggplot(data=df, aes(x=VERS, y=LOC, group=1)) +
  geom_line()+ geom_point() + geom_line(aes(y = mean(LOC)), color = "red", linetype = "solid") + theme(axis.text.x=element_blank())

noc_plot <- ggplot(data=df, aes(x=VERS, y=NOC, group=1)) +
  geom_line()+ geom_point() + geom_line(aes(y = mean(NOC)), color = "red", linetype = "solid") + theme(axis.text.x=element_blank())

nof_plot <- ggplot(data=df, aes(x=VERS, y=NOF, group=1)) +
  geom_line()+ geom_point() + geom_line(aes(y = mean(NOF)), color = "red", linetype = "solid") + theme(axis.text.x=element_blank())

nom_plot <- ggplot(data=df, aes(x=VERS, y=NOM, group=1)) +
  geom_line()+ geom_point()  + geom_line(aes(y = mean(NOM)), color = "red", linetype = "solid") + theme(axis.text.x=element_blank())

stat_plot <- ggplot(data=df, aes(x=VERS, y=STAT, group=1)) +
  geom_line()+ geom_point() + geom_line(aes(y = mean(STAT)), color = "red", linetype = "solid") + theme(axis.text.x=element_blank())

change_value

ntr_value

mrr_value

tsv_value

ggsave(ntc_plot, filename = "sing/NTC.pdf")
ggsave(ttl_plot, filename = "sing/TTL.pdf")
ggsave(tlr_plot, filename = "sing/TLR.pdf")
ggsave(mtlr_plot, filename = "sing/MTLR.pdf")
ggsave(mrtl_plot, filename = "sing/MRTL.pdf")
ggsave(mcr_plot, filename = "sing/MCR.pdf")
ggsave(mmr_plot, filename = "sing/MMR.pdf")
ggsave(mcmmr_plot, filename = "sing/MCMMR.pdf")
ggsave(cc_plot, filename = "sing/CC.pdf")
ggsave(codesmells_plot, filename = "sing/CODESMELLS.pdf")
ggsave(td_plot, filename = "sing/TD.pdf")
ggsave(debtratio_plot, filename = "sing/DEBTRATIO.pdf")
ggsave(cloc_plot, filename = "sing/CLOC.pdf")
ggsave(loc_plot, filename = "sing/LOC.pdf")
ggsave(noc_plot, filename = "sing/NOC.pdf")
ggsave(nof_plot, filename = "sing/NOF.pdf")
ggsave(nom_plot, filename = "sing/NOM.pdf")
ggsave(stat_plot, filename = "sing/STAT.pdf")