library(dplyr)
library(ggplot2)
library(stringr)
library(gsubfn)
library(Hmisc)
library(broom)
library(ggpubr)
dir.root <- "C:/Users/frenk/Documents/R-test data"
dir.origine_uno <- paste(dir.root, "in1", sep="/")
dir.origine_due <- paste(dir.root, "in2", sep="/")
dir.destinazione <- paste(dir.root, "out", sep="/")

files1 <- list.files(path=dir.origine_uno, pattern="csv$")
files2 <- list.files(path=dir.origine_due, pattern="csv$")

nomefile.dest=paste("Metriche_output","csv", sep=".");
nomefile.completo=paste(dir.destinazione, nomefile.dest, sep="/")
v_start <- c("Progetto",";", "Metrica", ";", "Valore", "\n")
write.table(v_start, nomefile.completo, eol = "", append=FALSE, row.names=FALSE, col.names=FALSE, sep=";", quote=FALSE)

nomefile.dest_ult=paste("Output_completo","csv", sep=".");
nomefile.completo_ult=paste(dir.destinazione, nomefile.dest_ult, sep="/")
v_start_ult <- c("Progetto",";", "CC", ";", "CHANGE", ";", "CODE_SMELLS", ";", "TD", ";", "TDR", ";", "CLOC", ";", "LOC", ";", "NOC", ";", "NOF", ";", "NOM", ";", "STAT", ";", "NTC", ";", "TTL", ";", "TLR", ";", "MTLR", ";", "MRTL", ";", "MCR", ";", "MMR", ";", "MCMMR","\n")
write.table(v_start_ult, nomefile.completo_ult, eol = "", append=FALSE, row.names=FALSE, col.names=FALSE, sep=";", quote=FALSE)


v_ntr <- NULL
v_ntc <- NULL
v_ttl <- NULL
v_tlr <- NULL
v_mtlr <- NULL
v_mrtl <- NULL
v_mcr <- NULL
v_mmr <- NULL
v_mcmmr <- NULL
v_mrr <- NULL
v_tsv <- NULL

v_cc <- NULL
v_cloc <- NULL
v_loc <- NULL
v_noc <- NULL
v_nof <- NULL
v_nom <- NULL
v_stat <- NULL
v_change <- NULL
v_codesmells <- NULL
v_td <- NULL
v_debtratio <- NULL

for (f in files1)
{
  nomefile <- paste(dir.origine_uno, f, sep="/")
  df <- read.csv(nomefile)
  # NTC MEDIO
  ntc_pos = subset(df, NTC>0)
  avg_ntc = mean(ntc_pos$NTC)
  if (is.nan(avg_ntc))
    avg_ntc = 0.0
  v_ntc <- c(f,";", "NTC", ";", avg_ntc, "\n")
  write.table(v_ntc, nomefile.completo, eol = "", append=TRUE, row.names=FALSE, col.names=FALSE, sep=";", quote=FALSE)
  # TTL MEDIO
  ttl_pos = subset(df, TTL>0)
  avg_ttl = mean(ttl_pos$TTL)
  if (is.nan(avg_ttl))
    avg_ttl = 0.0
  v_ttl <- c(f,";", "TTL", ";", avg_ttl, "\n")
  write.table(v_ttl, nomefile.completo, eol = "", append=TRUE, row.names=FALSE, col.names=FALSE, sep=";", quote=FALSE)
  # TRL MEDIO
  tlr_pos = subset(df, TLR>0)
  avg_tlr = mean(tlr_pos$TLR)
  if (is.nan(avg_tlr))
    avg_tlr = 0.0
  v_tlr <- c(f,";", "TLR", ";", avg_tlr, "\n")
  write.table(v_tlr, nomefile.completo, eol = "", append=TRUE, row.names=FALSE, col.names=FALSE, sep=";", quote=FALSE)
  
  # MTLR MEDIO
  mtlr_pos = subset(df, MTLR>0)
  avg_mtlr = mean(mtlr_pos$MTLR)
  if (is.nan(avg_mtlr))
    avg_mtlr = 0.0
  v_mtlr <- c(f,";", "MTLR", ";", avg_mtlr, "\n")
  write.table(v_mtlr, nomefile.completo, eol = "", append=TRUE, row.names=FALSE, col.names=FALSE, sep=";", quote=FALSE)
  
  # MRTL MEDIO
  mrtl_pos = subset(df, MRTL>0)
  avg_mrtl = mean(mrtl_pos$MRTL)
  if (is.nan(avg_mrtl))
    avg_mrtl = 0.0
  v_mrtl <- c(f,";", "MRTL", ";", avg_mrtl, "\n")
  write.table(v_mrtl, nomefile.completo, eol = "", append=TRUE, row.names=FALSE, col.names=FALSE, sep=";", quote=FALSE)
  
  # MCR MEDIO
  mcr_pos = subset(df, MCR>0)
  avg_mcr = mean(mcr_pos$MCR)
  if (is.nan(avg_mcr))
    avg_mcr = 0.0
  v_mcr <- c(f,";", "MCR", ";", avg_mcr, "\n")
  write.table(v_mcr, nomefile.completo, eol = "", append=TRUE, row.names=FALSE, col.names=FALSE, sep=";", quote=FALSE)
  
  # MMR MEDIO
  mmr_pos = subset(df, MMR>0)
  avg_mmr = mean(mmr_pos$MMR)
  if (is.nan(avg_mmr))
    avg_mmr = 0.0
  v_mmr <- c(f,";", "MMR", ";", avg_mmr, "\n")
  write.table(v_mmr, nomefile.completo, eol = "", append=TRUE, row.names=FALSE, col.names=FALSE, sep=";", quote=FALSE)
  
  # MCMMR MEDIO
  mcmmr_pos = subset(df, MCMMR>0)
  avg_mcmmr = mean(mcmmr_pos$MCMMR)
  if (is.nan(avg_mcmmr))
    avg_mcmmr = 0.0
  v_mcmmr <- c(f,";", "MCMMR", ";", avg_mcmmr, "\n")
  write.table(v_mcmmr, nomefile.completo, eol = "", append=TRUE, row.names=FALSE, col.names=FALSE, sep=";", quote=FALSE)
  
  # CC MEDIO
  cc_pos = subset(df, CC>0)
  avg_cc = mean(cc_pos$CC)
  if (is.nan(avg_cc))
    avg_cc = 0.0
  v_cc <- c(f, ";", "CC", ";", avg_cc, "\n")
  write.table(v_cc, nomefile.completo, eol = "", append=TRUE, row.names=FALSE, col.names=FALSE, sep=";", quote=FALSE)
  
  # CHANGE MEDIO
  change_pos = subset(df, CHANGE!="")
  change_temp = (change_pos$CHANGE)
  only_numbers = strapply(change_temp, "[(](\\d+)[)]", simplify=TRUE)
  sum_change = 0
  for (i in only_numbers)
    for (j in i)
      sum_change = sum_change + as.integer(j)
  if (is.nan(sum_change))
    sum_change = 0.0
  v_change <- c(f, ";", "CHANGE", ";", sum_change, "\n")
  write.table(v_change, nomefile.completo, eol = "", append=TRUE, row.names=FALSE, col.names=FALSE, sep=";", quote=FALSE)
  
  # CODE_SMELLS MEDIO
  codesmells_pos = subset(df, CODE_SMELLS>0)
  avg_codesmells = mean(codesmells_pos$CODE_SMELLS)
  if (is.nan(avg_codesmells))
    avg_codesmells = 0.0
  v_codesmells <- c(f,";", "CODE_SMELLS", ";", avg_codesmells, "\n")
  write.table(v_codesmells, nomefile.completo, eol = "", append=TRUE, row.names=FALSE, col.names=FALSE, sep=";", quote=FALSE)
  
  # TD MEDIO
  td_pos = subset(df, TD>0)
  avg_td = mean(td_pos$TD)
  if (is.nan(avg_td))
    avg_td = 0.0
  v_td <- c(f,";", "TD", ";", avg_td, "\n")
  write.table(v_td, nomefile.completo, eol = "", append=TRUE, row.names=FALSE, col.names=FALSE, sep=";", quote=FALSE)
  
  # DEBT_RATIO MEDIO
  debtratio_pos = subset(df, DEBT_RATIO>0)
  avg_debtratio = mean(debtratio_pos$DEBT_RATIO)
  if (is.nan(avg_debtratio))
    avg_debtratio = 0.0
  v_debtratio <- c(f,";", "TDR", ";", avg_debtratio, "\n")
  write.table(v_debtratio, nomefile.completo, eol = "", append=TRUE, row.names=FALSE, col.names=FALSE, sep=";", quote=FALSE)
  
  # CLOC MEDIO
  cloc_pos = subset(df, CLOC>0)
  avg_cloc = mean(cloc_pos$CLOC)
  if (is.nan(avg_cloc))
    avg_cloc = 0.0
  v_cloc <- c(f,";", "CLOC", ";", avg_cloc, "\n")
  write.table(v_cloc, nomefile.completo, eol = "", append=TRUE, row.names=FALSE, col.names=FALSE, sep=";", quote=FALSE)
  
  # LOC MEDIO
  loc_pos = subset(df, LOC>0)
  avg_loc = mean(loc_pos$LOC)
  if (is.nan(avg_loc))
    avg_loc = 0.0
  v_loc <- c(f,";", "LOC", ";", avg_loc, "\n")
  write.table(v_loc, nomefile.completo, eol = "", append=TRUE, row.names=FALSE, col.names=FALSE, sep=";", quote=FALSE)
  
  # NOC MEDIO
  noc_pos = subset(df, NOC>0)
  avg_noc = mean(noc_pos$NOC)
  if (is.nan(avg_noc))
    avg_noc = 0.0
  v_noc <- c(f,";", "NOC", ";", avg_noc, "\n")
  write.table(v_noc, nomefile.completo, eol = "", append=TRUE, row.names=FALSE, col.names=FALSE, sep=";", quote=FALSE)
  
  # NOF MEDIO
  nof_pos = subset(df, NOF>0)
  avg_nof = mean(nof_pos$NOF)
  if (is.nan(avg_nof))
    avg_nof = 0.0
  v_nof <- c(f,";", "NOF", ";", avg_nof, "\n")
  write.table(v_nof, nomefile.completo, eol = "", append=TRUE, row.names=FALSE, col.names=FALSE, sep=";", quote=FALSE)
  
  # NOM MEDIO
  nom_pos = subset(df, NOM>0)
  avg_nom = mean(nom_pos$NOM)
  if (is.nan(avg_nom))
    avg_nom = 0.0
  v_nom <- c(f,";", "NOM", ";", avg_nom, "\n")
  write.table(v_nom, nomefile.completo, eol = "", append=TRUE, row.names=FALSE, col.names=FALSE, sep=";", quote=FALSE)
  
  # STAT MEDIO
  stat_pos = subset(df, STAT>0)
  avg_stat = mean(stat_pos$STAT)
  if (is.nan(avg_stat))
    avg_stat = 0.0
  v_stat <- c(f,";", "STAT", ";", avg_stat, "\n")
  write.table(v_stat, nomefile.completo, eol = "", append=TRUE, row.names=FALSE, col.names=FALSE, sep=";", quote=FALSE)
  
  v_finefile <- NULL
  v_finefile <- c(f,";", avg_cc, ";", sum_change, ";", avg_codesmells, ";", avg_td, ";", avg_debtratio, ";", avg_cloc, ";", avg_loc, ";", avg_noc, ";", avg_nof, ";", avg_nom, ";", avg_stat, ";", avg_ntc, ";", avg_ttl, ";", avg_tlr, ";", avg_mtlr, ";", avg_mrtl, ";", avg_mcr, ";", avg_mmr, ";", avg_mcmmr, "\n")
  write.table(v_finefile, nomefile.completo_ult, eol = "", append=TRUE, row.names=FALSE, col.names=FALSE, sep=";", quote=FALSE)
  
  
}

for (f in files2)
{
  nomefile <- paste(dir.origine_due, f, sep="/")
  df <- read.csv(nomefile)
  # MRR
  mrr = (df$MRR)
  if (is.nan(mrr))
    mrr = 0.0
  v_mrr <- c(f,";", "MRR", ";", mrr, "\n")
  write.table(v_mrr, nomefile.completo, eol = "", append=TRUE, row.names=FALSE, col.names=FALSE, sep=";", quote=FALSE)
  
  # TSV
  tsv = (df$TSV)
  if (is.nan(tsv))
    tsv = 0.0
  v_tsv <- c(f,";", "TSV", ";", tsv, "\n")
  write.table(v_tsv, nomefile.completo, eol = "", append=TRUE, row.names=FALSE, col.names=FALSE, sep=";", quote=FALSE)
  
  # NTR
  ntr = (df$NTR)
  if (is.nan(ntr))
    ntr = 0.0
  v_ntr <- c(f,";", "NTR", ";", ntr, "\n")
  write.table(v_ntr, nomefile.completo, eol = "", append=TRUE, row.names=FALSE, col.names=FALSE, sep=";", quote=FALSE)
  
}

data <- read.csv(nomefile.completo, header = TRUE, sep = ";")

data_1 = data %>% filter(Metrica == "NTR" | Metrica == "NTC")
data_2 = data %>% filter(Metrica == "TTL")
data_3 = data %>% filter(Metrica == "TLR")
data_4 = data %>% filter(Metrica == "MTLR")
data_5 = data %>% filter(Metrica == "MRTL" | Metrica == "MCR" | Metrica == "MMR" | Metrica == "MCMMR" | Metrica == "MRR" | Metrica == "TSV")
data_6 = data %>% filter(Metrica == "CC")
data_7 = data %>% filter(Metrica == "CLOC" | Metrica == "LOC" | Metrica == "STAT")
data_8 = data %>% filter(Metrica == "NOC" | Metrica == "NOF" | Metrica == "NOM")
data_9 = data %>% filter(Metrica == "CHANGE")
data_10 = data %>% filter(Metrica == "CODE_SMELLS")
data_11 = data %>% filter(Metrica == "TD")
data_12 = data %>% filter(Metrica == "TDR")

data_1 %>%
  group_by(Metrica) %>%
  summarise_at(vars(Valore), funs(mean(., na.rm=TRUE)))

data_2 %>%
  group_by(Metrica) %>%
  summarise_at(vars(Valore), funs(mean(., na.rm=TRUE)))

data_3 %>%
  group_by(Metrica) %>%
  summarise_at(vars(Valore), funs(mean(., na.rm=TRUE)))

data_4 %>%
  group_by(Metrica) %>%
  summarise_at(vars(Valore), funs(mean(., na.rm=TRUE)))

data_5 %>%
  group_by(Metrica) %>%
  summarise_at(vars(Valore), funs(mean(., na.rm=TRUE)))

data_6 %>%
  group_by(Metrica) %>%
  summarise_at(vars(Valore), funs(mean(., na.rm=TRUE)))

data_7 %>%
  group_by(Metrica) %>%
  summarise_at(vars(Valore), funs(mean(., na.rm=TRUE)))

data_8 %>%
  group_by(Metrica) %>%
  summarise_at(vars(Valore), funs(mean(., na.rm=TRUE)))

data_9 %>%
  group_by(Metrica) %>%
  summarise_at(vars(Valore), funs(mean(., na.rm=TRUE)))

data_10 %>%
  group_by(Metrica) %>%
  summarise_at(vars(Valore), funs(mean(., na.rm=TRUE)))

data_11 %>%
  group_by(Metrica) %>%
  summarise_at(vars(Valore), funs(mean(., na.rm=TRUE)))

data_12 %>%
  group_by(Metrica) %>%
  summarise_at(vars(Valore), funs(mean(., na.rm=TRUE)))

ggplot_fragilit�_1 <- ggplot(data=data_1, aes(x=factor(Metrica, levels = c("NTR", "NTC")), y=Valore, fill=Metrica)) +
  geom_violin(trim=TRUE, color='#999999')  + xlab("Metriche") + ylab("Valori")+ theme(legend.position = 'none') + stat_summary(fun=mean, geom="point", shape=23, size=2) + stat_summary(fun=median, geom="point", size=2, color="red") + scale_fill_brewer(palette="Set3")

ggplot_fragilit�_2 <- ggplot(data=data_2, aes(x=factor(Metrica, levels = c("TTL")), y=Valore, fill=Metrica)) +
  geom_violin(trim=TRUE, color='#999999')  + xlab("Metriche") + ylab("Valori")+ theme(legend.position = 'none') + stat_summary(fun=mean, geom="point", shape=23, size=2) + stat_summary(fun=median, geom="point", size=2, color="red") + scale_fill_brewer(palette="Set3")

ggplot_fragilit�_3 <- ggplot(data=data_3, aes(x=factor(Metrica, levels = c("TLR")), y=Valore, fill=Metrica)) +
  geom_violin(trim=TRUE, color='#999999')  + xlab("Metriche") + ylab("Valori")+ theme(legend.position = 'none') + stat_summary(fun=mean, geom="point", shape=23, size=2) + stat_summary(fun=median, geom="point", size=2, color="red") + scale_fill_brewer(palette="Set3")

ggplot_fragilit�_4 <- ggplot(data=data_4, aes(x=factor(Metrica, levels = c("MTLR")), y=Valore, fill=Metrica)) +
  geom_violin(trim=TRUE, color='#999999')  + xlab("Metriche") + ylab("Valori")+ theme(legend.position = 'none') + scale_y_continuous(limits = c(NA, 4.75)) + stat_summary(fun=mean, geom="point", shape=23, size=2) + stat_summary(fun=median, geom="point", size=2, color="red") + scale_fill_brewer(palette="Set3")

ggplot_fragilit�_5 <- ggplot(data=data_5, aes(x=factor(Metrica, levels = c("MTLR", "MRTL", "MCR", "MMR", "MCMMR", "MRR", "TSV")), y=Valore, fill=Metrica)) +
  geom_violin(trim=TRUE, color='#999999')  + xlab("Metriche") + ylab("Valori")+ theme(legend.position = 'none') + stat_summary(fun=mean, geom="point", shape=23, size=2) + stat_summary(fun=median, geom="point", size=2, color="red") + scale_fill_brewer(palette="Set3")

ggplot_qualit�_1 <- ggplot(data=data_6, aes(x=factor(Metrica, levels = c("CC")), y=Valore, fill=Metrica)) +
  geom_violin(trim=TRUE, color='#999999')  + xlab("Metriche") + ylab("Valori")+ theme(legend.position = 'none') + stat_summary(fun=mean, geom="point", shape=23, size=2) + stat_summary(fun=median, geom="point", size=2, color="red") + scale_fill_brewer(palette="Set3")

ggplot_qualit�_2 <- ggplot(data=data_7, aes(x=factor(Metrica, levels = c("CLOC", "LOC", "STAT")), y=Valore, fill=Metrica)) +
  geom_violin(trim=TRUE, color='#999999')  + xlab("Metriche") + ylab("Valori")+ theme(legend.position = 'none') + stat_summary(fun=mean, geom="point", shape=23, size=2) + stat_summary(fun=median, geom="point", size=2, color="red") + scale_fill_brewer(palette="Set3")

ggplot_qualit�_3 <- ggplot(data=data_8, aes(x=factor(Metrica, levels = c("NOC", "NOF", "NOM")), y=Valore, fill=Metrica)) +
  geom_violin(trim=TRUE, color='#999999')  + xlab("Metriche") + ylab("Valori")+ theme(legend.position = 'none') + stat_summary(fun=mean, geom="point", shape=23, size=2) + stat_summary(fun=median, geom="point", size=2, color="red") + scale_fill_brewer(palette="Set3")

ggplot_qualit�_4 <- ggplot(data=data_9, aes(x=factor(Metrica, levels = c("CHANGE")), y=Valore, fill=Metrica)) +
  geom_violin(trim=TRUE, color='#999999')  + xlab("Metriche") + ylab("Valori")+ theme(legend.position = 'none') + stat_summary(fun=mean, geom="point", shape=23, size=2) + stat_summary(fun=median, geom="point", size=2, color="red") + scale_fill_brewer(palette="Set3")

ggplot_qualit�_5 <- ggplot(data=data_10, aes(x=factor(Metrica, levels = c("CODE_SMELLS")), y=Valore, fill=Metrica)) +
  geom_violin(trim=TRUE, color='#999999')  + xlab("Metriche") + ylab("Valori")+ theme(legend.position = 'none') + stat_summary(fun=mean, geom="point", shape=23, size=2) + stat_summary(fun=median, geom="point", size=2, color="red") + scale_fill_brewer(palette="Set3")

ggplot_qualit�_6 <- ggplot(data=data_11, aes(x=factor(Metrica, levels = c("TD")), y=Valore, fill=Metrica)) +
  geom_violin(trim=TRUE, color='#999999')  + xlab("Metriche") + ylab("Valori")+ theme(legend.position = 'none') + stat_summary(fun=mean, geom="point", shape=23, size=2) + stat_summary(fun=median, geom="point", size=2, color="red") + scale_fill_brewer(palette="Set3")

ggplot_qualit�_7 <- ggplot(data=data_12, aes(x=factor(Metrica, levels = c("TDR")), y=Valore, fill=Metrica)) +
  geom_violin(trim=TRUE, color='#999999')  + xlab("Metriche") + ylab("Valori")+ theme(legend.position = 'none') + stat_summary(fun=mean, geom="point", shape=23, size=2) + stat_summary(fun=median, geom="point", size=2, color="red") + scale_fill_brewer(palette="Set3")

ggplot_fragilit�_1
ggplot_fragilit�_2
ggplot_fragilit�_3
ggplot_fragilit�_4
ggplot_fragilit�_5
ggplot_qualit�_1
ggplot_qualit�_2
ggplot_qualit�_3
ggplot_qualit�_4
ggplot_qualit�_5
ggplot_qualit�_6
ggplot_qualit�_7

ggsave(ggplot_fragilit�_1, filename = "NTR_NTC.pdf")
ggsave(ggplot_fragilit�_2, filename = "TTL.pdf", width=4)
ggsave(ggplot_fragilit�_3, filename = "TLR.pdf", width=4)
ggsave(ggplot_fragilit�_4, filename = "MTLR.pdf", width=4)
ggsave(ggplot_fragilit�_5, filename = "MRTL_MCR_MMR_MCMMR_MRR_TSV.pdf")
ggsave(ggplot_qualit�_1, filename = "CC.pdf", width=4)
ggsave(ggplot_qualit�_2, filename = "CLOC_LOC_STAT.pdf")
ggsave(ggplot_qualit�_3, filename = "NOC_NOF_NOM.pdf")
ggsave(ggplot_qualit�_4, filename = "CHANGE.pdf", width=4)
ggsave(ggplot_qualit�_5, filename = "CODESMELLS.pdf", width=4)
ggsave(ggplot_qualit�_6, filename = "TD.pdf", width=4)
ggsave(ggplot_qualit�_7, filename = "TDR.pdf", width=4)

#-------------------------------------------------------------------------------#
  
med_median_1 = data %>% filter(Metrica == "NTR")
med_median_2 = data %>% filter(Metrica == "NTC")
med_median_3 = data %>% filter(Metrica == "TTL")
med_median_4 = data %>% filter(Metrica == "TLR")
med_median_5 = data %>% filter(Metrica == "MRTL")
med_median_6 = data %>% filter(Metrica == "MCR")
med_median_7 = data %>% filter(Metrica == "MMR")
med_median_8 = data %>% filter(Metrica == "MCMMR")
med_median_9 = data %>% filter(Metrica == "MRR")
med_median_10 = data %>% filter(Metrica == "TSV")
med_median_11 = data %>% filter(Metrica == "MTLR")
med_median_12 = data %>% filter(Metrica == "CC")
med_median_13 = data %>% filter(Metrica == "CODE_SMELLS")
med_median_14 = data %>% filter(Metrica == "TD")
med_median_15 = data %>% filter(Metrica == "TDR")
med_median_16 = data %>% filter(Metrica == "CLOC")
med_median_17 = data %>% filter(Metrica == "LOC")
med_median_18 = data %>% filter(Metrica == "NOC")
med_median_19 = data %>% filter(Metrica == "NOF")
med_median_20 = data %>% filter(Metrica == "NOM")
med_median_21 = data %>% filter(Metrica == "STAT")
med_median_22 = data %>% filter(Metrica == "CHANGE")

nomefile.dest2=paste("Medie_output","csv", sep=".");
nomefile.completo2=paste(dir.destinazione, nomefile.dest2, sep="/")
v_start2 <- c("Metrica",";", "Media", ";", "Mediana", "\n")
write.table(v_start2, nomefile.completo2, eol = "", append=FALSE, row.names=FALSE, col.names=FALSE, sep=";", quote=FALSE)
val_1 <- mean(med_median_1$Valore)
val_2 <- median(med_median_1$Valore)
med_1 <- c("NTR",";", val_1, ";", val_2, "\n")
write.table(med_1, nomefile.completo2, eol = "", append=TRUE, row.names=FALSE, col.names=FALSE, sep=";", quote=FALSE)

val_1 <- mean(med_median_2$Valore)
val_2 <- median(med_median_2$Valore)
med_2 <- c("NTC",";", val_1, ";", val_2, "\n")
write.table(med_2, nomefile.completo2, eol = "", append=TRUE, row.names=FALSE, col.names=FALSE, sep=";", quote=FALSE)

val_1 <- mean(med_median_3$Valore)
val_2 <- median(med_median_3$Valore)
med_3 <- c("TTL",";", val_1, ";", val_2, "\n")
write.table(med_3, nomefile.completo2, eol = "", append=TRUE, row.names=FALSE, col.names=FALSE, sep=";", quote=FALSE)

val_1 <- mean(med_median_4$Valore)
val_2 <- median(med_median_4$Valore)
med_4 <- c("TLR",";", val_1, ";", val_2, "\n")
write.table(med_4, nomefile.completo2, eol = "", append=TRUE, row.names=FALSE, col.names=FALSE, sep=";", quote=FALSE)

val_1 <- mean(med_median_5$Valore)
val_2 <- median(med_median_5$Valore)
med_5 <- c("MRTL",";", val_1, ";", val_2, "\n")
write.table(med_5, nomefile.completo2, eol = "", append=TRUE, row.names=FALSE, col.names=FALSE, sep=";", quote=FALSE)

val_1 <- mean(med_median_6$Valore)
val_2 <- median(med_median_6$Valore)
med_6 <- c("MCR",";", val_1, ";", val_2, "\n")
write.table(med_6, nomefile.completo2, eol = "", append=TRUE, row.names=FALSE, col.names=FALSE, sep=";", quote=FALSE)

val_1 <- mean(med_median_7$Valore)
val_2 <- median(med_median_7$Valore)
med_7 <- c("MMR",";", val_1, ";", val_2, "\n")
write.table(med_7, nomefile.completo2, eol = "", append=TRUE, row.names=FALSE, col.names=FALSE, sep=";", quote=FALSE)

val_1 <- mean(med_median_8$Valore)
val_2 <- median(med_median_8$Valore)
med_8 <- c("MCMMR",";", val_1, ";", val_2, "\n")
write.table(med_8, nomefile.completo2, eol = "", append=TRUE, row.names=FALSE, col.names=FALSE, sep=";", quote=FALSE)

val_1 <- mean(med_median_9$Valore)
val_2 <- median(med_median_9$Valore)
med_9 <- c("MRR",";", val_1, ";", val_2, "\n")
write.table(med_9, nomefile.completo2, eol = "", append=TRUE, row.names=FALSE, col.names=FALSE, sep=";", quote=FALSE)

val_1 <- mean(med_median_10$Valore)
val_2 <- median(med_median_10$Valore)
med_10 <- c("TSV",";", val_1, ";", val_2, "\n")
write.table(med_10, nomefile.completo2, eol = "", append=TRUE, row.names=FALSE, col.names=FALSE, sep=";", quote=FALSE)

val_1 <- mean(med_median_11$Valore)
val_2 <- median(med_median_11$Valore)
med_11 <- c("MTLR",";", val_1, ";", val_2, "\n")
write.table(med_11, nomefile.completo2, eol = "", append=TRUE, row.names=FALSE, col.names=FALSE, sep=";", quote=FALSE)

val_1 <- mean(med_median_12$Valore)
val_2 <- median(med_median_12$Valore)
med_12 <- c("CC",";", val_1, ";", val_2, "\n")
write.table(med_12, nomefile.completo2, eol = "", append=TRUE, row.names=FALSE, col.names=FALSE, sep=";", quote=FALSE)

val_1 <- mean(med_median_13$Valore)
val_2 <- median(med_median_13$Valore)
med_13 <- c("CODE_SMELLS",";", val_1, ";", val_2, "\n")
write.table(med_13, nomefile.completo2, eol = "", append=TRUE, row.names=FALSE, col.names=FALSE, sep=";", quote=FALSE)

val_1 <- mean(med_median_14$Valore)
val_2 <- median(med_median_14$Valore)
med_14 <- c("TD",";", val_1, ";", val_2, "\n")
write.table(med_14, nomefile.completo2, eol = "", append=TRUE, row.names=FALSE, col.names=FALSE, sep=";", quote=FALSE)

val_1 <- mean(med_median_15$Valore)
val_2 <- median(med_median_15$Valore)
med_15 <- c("TDR",";", val_1, ";", val_2, "\n")
write.table(med_15, nomefile.completo2, eol = "", append=TRUE, row.names=FALSE, col.names=FALSE, sep=";", quote=FALSE)

val_1 <- mean(med_median_16$Valore)
val_2 <- median(med_median_16$Valore)
med_16 <- c("CLOC",";", val_1, ";", val_2, "\n")
write.table(med_16, nomefile.completo2, eol = "", append=TRUE, row.names=FALSE, col.names=FALSE, sep=";", quote=FALSE)

val_1 <- mean(med_median_17$Valore)
val_2 <- median(med_median_17$Valore)
med_17 <- c("LOC",";", val_1, ";", val_2, "\n")
write.table(med_17, nomefile.completo2, eol = "", append=TRUE, row.names=FALSE, col.names=FALSE, sep=";", quote=FALSE)

val_1 <- mean(med_median_18$Valore)
val_2 <- median(med_median_18$Valore)
med_18 <- c("NOC",";", val_1, ";", val_2, "\n")
write.table(med_18, nomefile.completo2, eol = "", append=TRUE, row.names=FALSE, col.names=FALSE, sep=";", quote=FALSE)

val_1 <- mean(med_median_19$Valore)
val_2 <- median(med_median_19$Valore)
med_19 <- c("NOF",";", val_1, ";", val_2, "\n")
write.table(med_19, nomefile.completo2, eol = "", append=TRUE, row.names=FALSE, col.names=FALSE, sep=";", quote=FALSE)

val_1 <- mean(med_median_20$Valore)
val_2 <- median(med_median_20$Valore)
med_20 <- c("NOM",";", val_1, ";", val_2, "\n")
write.table(med_20, nomefile.completo2, eol = "", append=TRUE, row.names=FALSE, col.names=FALSE, sep=";", quote=FALSE)

val_1 <- mean(med_median_21$Valore)
val_2 <- median(med_median_21$Valore)
med_21 <- c("STAT",";", val_1, ";", val_2, "\n")
write.table(med_21, nomefile.completo2, eol = "", append=TRUE, row.names=FALSE, col.names=FALSE, sep=";", quote=FALSE)

val_1 <- mean(med_median_22$Valore)
val_2 <- median(med_median_22$Valore)
med_22 <- c("CHANGE",";", val_1, ";", val_2, "\n")
write.table(med_22, nomefile.completo2, eol = "", append=TRUE, row.names=FALSE, col.names=FALSE, sep=";", quote=FALSE)


vector1 <- list()
vector2 <- list()
vector1[[1]] <- med_median_1$Valore
vector1[[2]] <- med_median_2$Valore
vector1[[3]] <- med_median_3$Valore
vector1[[4]] <- med_median_4$Valore
vector1[[5]] <- med_median_11$Valore
vector1[[6]] <- med_median_5$Valore
vector1[[7]] <- med_median_6$Valore
vector1[[8]] <- med_median_7$Valore
vector1[[9]] <- med_median_8$Valore
vector1[[10]] <- med_median_9$Valore
vector1[[11]] <- med_median_10$Valore

vector2[[1]] <- med_median_12$Valore
vector2[[2]] <- med_median_22$Valore
vector2[[3]] <- med_median_13$Valore
vector2[[4]] <- med_median_14$Valore
vector2[[5]] <- med_median_15$Valore
vector2[[6]] <- med_median_16$Valore
vector2[[7]] <- med_median_17$Valore
vector2[[8]] <- med_median_18$Valore
vector2[[9]] <- med_median_19$Valore
vector2[[10]] <- med_median_20$Valore
vector2[[11]] <- med_median_21$Valore

nomefile.dest3=paste("Correlazione","csv", sep=".");
nomefile.completo3=paste(dir.destinazione, nomefile.dest3, sep="/")
v_start3 <- c("Metrica",";", "NTR", ";", "NTC",";", "TTL",";", "TLR", ";", "MTLR",";", "MRTL",";", "MCR", ";", "MMR",";", "MCMMR",";", "MRR", ";", "TSV","\n")
write.table(v_start3, nomefile.completo3, eol = "", append=FALSE, row.names=FALSE, col.names=FALSE, sep=";", quote=FALSE)

metrica_temp=c("CC", "CHANGE", "CODE_SMELLS", "TD", "TDR", "CLOC", "LOC", "NOC", "NOF", "NOM", "STAT")

vector_res_riga <- NULL
contatore1 = c(1,2,3,4,5,6,7,8,9,10,11)
contatore2= c(1,2,3,4,5,6,7,8,9,10,11)
for (cont1 in contatore1)
{
  vector_res_riga <- NULL
  for (cont2 in contatore2)
  {
    lr <- lm(vector2[[cont1]] ~ vector1[[cont2]])
    pr_t_value <- summary(lr)$coefficients[2,4]
    vector_res_riga<- c(vector_res_riga, pr_t_value)
  }
  corr_1 <- c(metrica_temp[cont1],";", vector_res_riga[1], ";", vector_res_riga[2], ";", vector_res_riga[3], ";",vector_res_riga[4], ";", vector_res_riga[5], ";", vector_res_riga[6], ";", vector_res_riga[7], ";", vector_res_riga[8], ";", vector_res_riga[9], ";", vector_res_riga[10], ";", vector_res_riga[11],"\n")
  write.table(corr_1, nomefile.completo3, eol = "", append=TRUE, row.names=FALSE, col.names=FALSE, sep=";", quote=FALSE)
  
}

data_new <- read.csv(nomefile.completo_ult, header = TRUE, sep = ";")

scatter_1 <- ggplot(data=data_new, aes(x = MRTL, y = CC)) + geom_point(color="#47B8A4")
scatter_1_finale <- scatter_1 + geom_smooth(method="lm", color="darkred", fill="pink")

scatter_2 <- ggplot(data=data_new, aes(x = MTLR, y = CHANGE)) + geom_point(color="#47B8A4") + scale_x_continuous(limits = c(NA, 4.75))
scatter_2_finale <- scatter_2 + geom_smooth(method="lm", color="darkred", fill="pink")

scatter_3 <- ggplot(data=data_new, aes(x = TTL, y = CODE_SMELLS)) + geom_point(color="#47B8A4")
scatter_3_finale <- scatter_3 + geom_smooth(method="lm", color="darkred", fill="pink")

scatter_4 <- ggplot(data=data_new, aes(x = MCR, y = TD)) + geom_point(color="#47B8A4")
scatter_4_finale <- scatter_4 + geom_smooth(method="lm", color="darkred", fill="pink")

# scatter_4_finale + stat_regline_equation() # PER VEDERE RETTA

ggsave(scatter_1_finale, filename = "CORR_CC-MRTL.pdf")
ggsave(scatter_2_finale, filename = "CORR_CHANGE-MTLR.pdf")
ggsave(scatter_3_finale, filename = "CORR_CODESMELLS-TTL.pdf")
ggsave(scatter_4_finale, filename = "CORR_TD-MCR.pdf")