import os
import shutil
import subprocess
import csv
import requests
import sys
import pygount
from requests.auth import HTTPBasicAuth
import urllib.request, json
from time import sleep

import git
from git import Repo


# noinspection PyUnusedLocal
def readonly_handler(func, path, execinfo):
    os.chmod(path, 128)
    func(path)


def pulizia(repository):
    try:
        shutil.rmtree(repository, onerror=readonly_handler)
    except OSError as e:
        print("Error cleaning repository: %s : %s" % (repository, e.strerror), flush=True)


# PARTE 1: CLONARE PROGETTO DA GIT-HUB

rep_git = "git://github.com/"+sys.argv[1]
lastNReleases = int(sys.argv[2])
# commonsguy/cwac-merge"
rep_git_csv = "ScannerPythonTesi_"+sys.argv[1].replace("/","")
rep_git_csv1 = "ScannerPythonTesi"

test_tool_name=""
skip=0
if len(sys.argv)>3 :
    test_tool_name=sys.argv[3]
if len(test_tool_name) !=0 :
    with urllib.request.urlopen("https://api.github.com/search/code?q="+test_tool_name+"+repo:"+sys.argv[1]) as url:
        data = json.loads(url.read().decode())
        print("Total count: "+str(data["total_count"]))
    if data["total_count"] ==0 :
        skip=1

local_rep = '.\\input\\ProgettoDaAnalizzare\\'
local_rep_iminusone = '.\\input\\ProgettoDaAnalizzareVtag1\\'
local_rep_i = '.\\input\\ProgettoDaAnalizzareVtag2\\'
nome_progetto_sonarqube = rep_git_csv1
Repo.clone_from(rep_git, local_rep)

print('Script Python per calcolo di metriche di un progetto GitHub, ', rep_git, flush=True)

# PARTE 2: CALCOLO METRICHE FRAGILITA' TEST
# print('Calcolo metriche di fragilità di test:')

lr = git.Repo(local_rep)

# NTR (Number of Tagged Releases):
output_ntr = lr.git.tag(sort='creatordate')
tags = output_ntr.split('\n')
#print(tags)
print('NTR (Number of Tagged Releases): ', len(tags), flush=True)

with open('.\\input\\' + rep_git_csv + '.csv', 'w') as csvfile:
    writer = csv.writer(csvfile, delimiter=',')
    writer.writerow(("VERS", "NTC", "TTL", "TLR", "MTLR", "MRTL", "MCR", "MMR", "MCMMR", "CC", "CHANGE", "CODE_SMELLS",
                     "TD", "DEBT_RATIO", "CLOC", "LOC", "NOC", "NOF", "NOM", "STAT"))
with open('.\\input\\' + rep_git_csv + '_2.csv', 'w') as csvfile:
    writer = csv.writer(csvfile, delimiter=',')
    writer.writerow(("NTR", "MRR", "TSV"))
prima_volta = 0
ntct = []
test_classes_modified = []
counter_tcm = []
ntc_iminusone = []
tm_iminusone = 0
cont_taggedreleases_modified = 0
if len(tags) - lastNReleases <= 0 :
    cont_tags = 0
else:
    cont_tags = len(tags) - lastNReleases

vtag1 = tags[cont_tags]
cont_tags = cont_tags + 1
vtag2 = tags[cont_tags]
# print(vtag1, vtag2)

while cont_tags <= len(tags) - 1:
    # CLONARE PROGETTO/I
    if prima_volta == 0:
        Repo.clone_from(rep_git, local_rep_iminusone, branch=vtag1)
        Repo.clone_from(rep_git, local_rep_i, branch=vtag2)
        r_minusone = git.Repo(local_rep_iminusone)
        r = git.Repo(local_rep_i)
    else:
        r_minusone = git.Repo(local_rep_iminusone)
        r_minusone.git.reset("--hard")
        r_minusone.git.checkout(vtag1)
        r = git.Repo(local_rep_i)
        r.git.reset("--hard")
        r.git.checkout(vtag2)
    # print('prova', tags[cont_tags], len(tags))
    print("----------------------------------------------------------------------", flush=True)
    print("Metriche fragilita test (versioni %s ->- %s): " % (vtag1, vtag2), flush=True)
    print("----------------------------------------------------------------------", flush=True)
    # CALCOLO METRICHE
    if prima_volta == 0:
        prima_volta = 1
        # r_minusone = git.Repo(local_rep_iminusone)
        output_ntc = r_minusone.git.grep("-l", "")
        file_totali_progetto = output_ntc.split('\n')
        ntc = []
        cont_file = 0
        while cont_file < len(file_totali_progetto):
            if file_totali_progetto[cont_file].find('.java') != -1 and skip==0:
                if file_totali_progetto[cont_file].find('test') != -1:
                    ntc.append(file_totali_progetto[cont_file].split('\t')[-1])
                    try:
                        ntct.index(file_totali_progetto[cont_file].split('\t')[-1])
                        # print("Indice presente")
                    except:
                        # print("Aggiunta alla lista")
                        ntct.append(file_totali_progetto[cont_file].split('\t')[-1])
                elif file_totali_progetto[cont_file].find('Test') != -1:
                    ntc.append(file_totali_progetto[cont_file].split('\t')[-1])
                    try:
                        ntct.index(file_totali_progetto[cont_file].split('\t')[-1])
                        # print("Indice presente")
                    except:
                        # print("Aggiunta alla lista")
                        ntct.append(file_totali_progetto[cont_file].split('\t')[-1])
            elif file_totali_progetto[cont_file].find('.kt') != -1 and skip==0:
                if file_totali_progetto[cont_file].find('test') != -1:
                    ntc.append(file_totali_progetto[cont_file].split('\t')[-1])
                    try:
                        ntct.index(file_totali_progetto[cont_file].split('\t')[-1])
                        # print("Indice presente")
                    except:
                        # print("Aggiunta alla lista")
                        ntct.append(file_totali_progetto[cont_file].split('\t')[-1])
                elif file_totali_progetto[cont_file].find('Test') != -1:
                    ntc.append(file_totali_progetto[cont_file].split('\t')[-1])
                    try:
                        ntct.index(file_totali_progetto[cont_file].split('\t')[-1])
                        # print("Indice presente")
                    except:
                        # print("Aggiunta alla lista")
                        ntct.append(file_totali_progetto[cont_file].split('\t')[-1])
            cont_file = cont_file + 1
        # print(ntc)
        print('NTC (Number of Test Classes) %s: %d' % (vtag1, len(ntc)), flush=True)
        ntc_iminusone = ntc

        # TTL (Total Test LOCs):
        cont_ttl_pv = 0
        ttl = 0
        while cont_ttl_pv <= len(ntc) - 1:
            #risultati_cloc_ttl = subprocess.run([".\\input\\cloc.exe", "--quiet", local_rep_iminusone + ntc[cont_ttl_pv]], stdout=subprocess.PIPE)
            risultati_cloc_ttl = subprocess.run(["pygount", local_rep_iminusone + ntc[cont_ttl_pv] ,"--format=summary"], stdout=subprocess.PIPE)
            output_cloc_ttl = risultati_cloc_ttl.stdout.decode('utf8').split('\r\n')
            cont_out_ttl = 0
            # print(output_cloc_ttl)
            while cont_out_ttl < len(output_cloc_ttl):
                if output_cloc_ttl[cont_out_ttl].find("Java") != -1 and skip==0:
                    out_cloc_ttl_split = output_cloc_ttl[cont_out_ttl].split(" ")
                    output_cloc_loc_filtrato = list(filter(lambda a: a != '', out_cloc_ttl_split))
                    ttl = ttl + int(output_cloc_loc_filtrato[3])
                elif output_cloc_ttl[cont_out_ttl].find("Kotlin") != -1 and skip==0:
                    out_cloc_ttl_split = output_cloc_ttl[cont_out_ttl].split(" ")
                    output_cloc_loc_filtrato = list(filter(lambda a: a != '', out_cloc_ttl_split))
                    ttl = ttl + int(output_cloc_loc_filtrato[3])
                cont_out_ttl = cont_out_ttl + 1
            cont_ttl_pv = cont_ttl_pv + 1
        print('TTL (Total Test LOCs) %s: %.2f' % (vtag1, ttl), flush=True)

        #risultati_cloc_loc_v1 = subprocess.run([".\\input\\cloc.exe", "--quiet", local_rep_iminusone], stdout=subprocess.PIPE)
        risultati_cloc_loc_v1 = subprocess.run(["pygount", local_rep_iminusone ,"--format=summary"],stdout=subprocess.PIPE)
        #print(risultati_cloc_loc_v1.stdout,flush=True)
        output_cloc_loc = risultati_cloc_loc_v1.stdout.decode('utf8').split('\r\n')
        # print(output_cloc_loc)
        contout = 0
        plocsi = 0
        cloc = 0
        while contout < len(output_cloc_loc):
            if output_cloc_loc[contout].find("Java") != -1 and output_cloc_loc[contout].find("Javascript") == -1 and output_cloc_loc[contout].find("JavaScript") == -1 :
                output_cloc_loc_split = output_cloc_loc[contout].split(" ")
                output_cloc_loc_filtrato = list(filter(lambda a: a != '', output_cloc_loc_split))
                print(*output_cloc_loc_filtrato, sep=", ")
                plocsi = plocsi + int(output_cloc_loc_filtrato[3])
                cloc = cloc + int(output_cloc_loc_filtrato[5])
            elif output_cloc_loc[contout].find("Kotlin") != -1:
                output_cloc_loc_split = output_cloc_loc[contout].split(" ")
                output_cloc_loc_filtrato = list(filter(lambda a: a != '', output_cloc_loc_split))
                plocsi = plocsi + int(output_cloc_loc_filtrato[3])
                cloc = cloc + int(output_cloc_loc_filtrato[5])
            contout = contout + 1
        # print('Calcolo metriche di qualità del codice:') -> Prima versione progetto
        cc = 0
        td = 0
        nom = 0
        stat = 0
        code_smells = 0
        noc = 0
        nof = 0
        debt_ratio = 0
        changecsv = ''
        print("***************************************", flush=True)
        # 1. Copiare file .properties dentro cartella progetto vtag1
        file_prop = ".\\input\\sonar-project.properties"
        dove_cop_vtag1 = ".\\input\\ProgettoDaAnalizzareVtag1"
        copy_properties = subprocess.call(['copy', file_prop, dove_cop_vtag1],
                                          shell=True)
        # 2. Far partire scansione sonar-scanner.bat (con cambio di directory)
        start_scanner = subprocess.run(["cd", ".\\input\\ProgettoDaAnalizzareVtag1", "&&", "..\\sonar-scanner\\bin\\sonar-scanner.bat"],
                                       stdout=subprocess.PIPE, shell=True)
        # 3. analizzare risultati json
        sleep(30)
        url = "http://localhost:9000/api/measures/component?metricKeys=complexity,sqale_index,functions,statements," \
              "code_smells,classes,files,sqale_debt_ratio,ncloc,comment_lines&component=" + nome_progetto_sonarqube
        resp = requests.get(url, auth=HTTPBasicAuth('admin', 'admin1'))
        sonar_resp_json = resp.json()
        # print(sonar_resp_json)
        metriche = sonar_resp_json["component"]["measures"]
        # print(metriche)

        for i in metriche:
            if i['metric'] == 'complexity':
                # print(i['value'])
                cc = int(i['value'])
            if i['metric'] == 'sqale_index':
                # print(i['value'])
                td = int(i['value'])
            if i['metric'] == 'functions':
                # print(i['value'])
                nom = int(i['value'])
            if i['metric'] == 'statements':
                # print(i['value'])
                stat = int(i['value'])
            if i['metric'] == 'code_smells':
                # print(i['value'])
                code_smells = int(i['value'])
            if i['metric'] == 'classes':
                # print(i['value'])
                noc = int(i['value'])
            if i['metric'] == 'files':
                # print(i['value'])
                nof = int(i['value'])
            if i['metric'] == 'sqale_debt_ratio':
                # print(i['value'])
                debt_ratio = float(i['value'])

        # 4. stampare metriche
        print("----------------------------------------------------------------------", flush=True)
        print("Metriche qualità codice (versione %s): " % vtag1, flush=True)
        print("----------------------------------------------------------------------", flush=True)

        print("LOC (Lines of Code) %s: %d" % (vtag1, plocsi), flush=True)

        print("CLOC (Comment Lines of Code) %s: %d" % (vtag1, cloc), flush=True)

        print("CC (McCabe’s Cyclomatic Complexity) %s: %d" % (vtag1, cc), flush=True)
        print("TD (Technical Debt) %s: %d" % (vtag1, td), flush=True)
        print("NOM (Number of Methods) %s: %d" % (vtag1, nom), flush=True)
        print("STAT (Number of Statements) %s: %d" % (vtag1, stat), flush=True)
        print("CODE SMELLS %s: %d" % (vtag1, code_smells), flush=True)
        print("NOC (Number of Classes) %s: %d" % (vtag1, noc), flush=True)
        print("NOF (Number of Files) %s: %d" % (vtag1, nof), flush=True)
        print("TECHNICAL DEBT RATIO %s: %.2f" % (vtag1, debt_ratio), flush=True)

        del_sonar_properties = subprocess.run(
            ["cd", ".\\input\\ProgettoDaAnalizzareVtag1", "&&", "del", ".\\sonar-project.properties"],
            stdout=subprocess.PIPE, shell=True)
        with open('.\\input\\'+rep_git_csv + '.csv', 'a') as csvfile:
            writer = csv.writer(csvfile, delimiter=',')
            writer.writerow((vtag1, len(ntc), ttl, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, cc, "", code_smells, td,
                             debt_ratio, cloc, plocsi, noc, nof, nom, stat))
    # r = git.Repo(local_rep_i)

    # NTC (Number of Test Classes):
    # r = git.Repo(local_rep)
    # output = r.git.grep("-l", "@Test")
    # ntc = output.split('\n')
    output_ntc = r.git.grep("-l", "")
    file_totali_progetto = output_ntc.split('\n')

    ntc = []
    cont_file = 0
    while cont_file < len(file_totali_progetto):
        if file_totali_progetto[cont_file].find('.java') != -1 and skip==0:
            if file_totali_progetto[cont_file].find('test') != -1:
                ntc.append(file_totali_progetto[cont_file].split('\t')[-1])
                try:
                    ntct.index(file_totali_progetto[cont_file].split('\t')[-1])
                    # print("Indice presente")
                except:
                    # print("Aggiunta alla lista")
                    ntct.append(file_totali_progetto[cont_file].split('\t')[-1])
            elif file_totali_progetto[cont_file].find('Test') != -1:
                ntc.append(file_totali_progetto[cont_file].split('\t')[-1])
                try:
                    ntct.index(file_totali_progetto[cont_file].split('\t')[-1])
                    # print("Indice presente")
                except:
                    # print("Aggiunta alla lista")
                    ntct.append(file_totali_progetto[cont_file].split('\t')[-1])
        elif file_totali_progetto[cont_file].find('.kt') != -1 and skip==0:
            if file_totali_progetto[cont_file].find('test') != -1:
                ntc.append(file_totali_progetto[cont_file].split('\t')[-1])
                try:
                    ntct.index(file_totali_progetto[cont_file].split('\t')[-1])
                    # print("Indice presente")
                except:
                    # print("Aggiunta alla lista")
                    ntct.append(file_totali_progetto[cont_file].split('\t')[-1])
            elif file_totali_progetto[cont_file].find('Test') != -1:
                ntc.append(file_totali_progetto[cont_file].split('\t')[-1])
                try:
                    ntct.index(file_totali_progetto[cont_file].split('\t')[-1])
                    # print("Indice presente")
                except:
                    # print("Aggiunta alla lista")
                    ntct.append(file_totali_progetto[cont_file].split('\t')[-1])
        cont_file = cont_file + 1
    # print(ntc)
    print('NTC (Number of Test Classes) %s: %d' % (vtag2, len(ntc)), flush=True)

    # TTL (Total Test LOCs):
    cont_ttl = 0
    # ttl = 0
    ttl = 0
    while cont_ttl <= len(ntc) - 1:

        #risultati_cloc_ttl = subprocess.run([".\\input\\cloc.exe", "--quiet", local_rep_i + ntc[cont_ttl]], stdout=subprocess.PIPE)
        risultati_cloc_ttl = subprocess.run(["pygount", local_rep_i + ntc[cont_ttl],"--format=summary"], stdout=subprocess.PIPE)
        #print(risultati_cloc_ttl.stdout)
        output_cloc_ttl = risultati_cloc_ttl.stdout.decode('utf8').split('\r\n')
        # print(output_cloc_ttl)
        cont_out_ttl = 0

        while cont_out_ttl < len(output_cloc_ttl):
            if output_cloc_ttl[cont_out_ttl].find("Java") != -1 and skip==0:
                out_cloc_ttl_split = output_cloc_ttl[cont_out_ttl].split(" ")
                out_cloc_ttl_split_filtrato = list(filter(lambda a: a != '', out_cloc_ttl_split))
                # print(out_cloc_ttl_split[-1])
                ttl = ttl + int(out_cloc_ttl_split_filtrato[3])
            elif output_cloc_ttl[cont_out_ttl].find("Kotlin") != -1 and skip==0:
                out_cloc_ttl_split = output_cloc_ttl[cont_out_ttl].split(" ")
                out_cloc_ttl_split_filtrato = list(filter(lambda a: a != '', out_cloc_ttl_split))
                # print(out_cloc_ttl_split[-1])
                ttl = ttl + int(out_cloc_ttl_split_filtrato[3])
            cont_out_ttl = cont_out_ttl + 1
        cont_ttl = cont_ttl + 1
    # print('vecchio TTL (Total Test LOCs): ', ttl)
    print('TTL (Total Test LOCs) %s: %.2f' % (vtag2, ttl), flush=True)

    # TLR (Test LOCs Ratio):

    output = r.git.diff(vtag1, vtag2, "--numstat")
    diff = output.split('\n')
    cont_file = 0
    contatore_modifiche = 0
    contatore_modifiche_test = 0
    cont_test_classes_modified = 0
    diff_filtrata = []
    change = []
    counter_change = []
    flag_mrr = 0
    while cont_file < len(diff):
        stringajava = ".java"
        stringakotlin = ".kt"
        # print(diff[cont_file], diff[cont_file].find(stringajava), diff[cont_file].find(stringakotlin))
        if diff[cont_file].find(stringajava) != -1:
            diff_filtrata.append(diff[cont_file])
            file_mod = diff[cont_file].split('\t')
            # print(file_mod)
            for ipsilon in ntc_iminusone:
                if ipsilon == file_mod[2]:
                    cont_test_classes_modified = cont_test_classes_modified + 1
            if file_mod[2].find("test") != -1:
                contatore_modifiche_test = contatore_modifiche_test + (int(file_mod[0]) + int(file_mod[1]))
                flag_mrr = 1
                # cont_test_classes_modified = cont_test_classes_modified + 1
                try:
                    index = test_classes_modified.index(file_mod[2])
                    # print("Indice già presente")
                    counter_tcm[index] = counter_tcm[index] + 1
                except:
                    # print("Aggiunta alla lista")
                    test_classes_modified.append(file_mod[2])
                    counter_tcm.append(1)
            elif file_mod[2].find("Test") != -1:
                contatore_modifiche_test = contatore_modifiche_test + (int(file_mod[0]) + int(file_mod[1]))
                flag_mrr = 1
                # cont_test_classes_modified = cont_test_classes_modified + 1
                try:
                    test_classes_modified.index(file_mod[2])
                    # print("Indice presente")
                    counter_tcm[test_classes_modified.index(file_mod[2])] = counter_tcm[test_classes_modified.index(
                        file_mod[2])] + 1
                except:
                    # print("Aggiunta alla lista")
                    test_classes_modified.append(file_mod[2])
                    counter_tcm.append(1)
            contatore_modifiche = contatore_modifiche + (int(file_mod[0]) + int(file_mod[1]))
            # print(contatore_modifiche)
            # print(contatore_modifiche_test)
            # CHANGE metric
            try:
                change.index(file_mod[2])
                # print("Indice presente")
                counter_change[change.index(file_mod[2])] = counter_change[change.index(file_mod[2])] + (
                        int(file_mod[0]) + int(file_mod[1]))
            except:
                # print("Aggiunta alla lista")
                change.append(file_mod[2])
                counter_change.append((int(file_mod[0]) + int(file_mod[1])))
        elif diff[cont_file].find(stringakotlin) != -1:
            diff_filtrata.append(diff[cont_file])
            file_mod = diff[cont_file].split('\t')
            # print(file_mod)
            for ipsilon in ntc_iminusone:
                if ipsilon == file_mod[2]:
                    cont_test_classes_modified = cont_test_classes_modified + 1
            if file_mod[2].find("test") != -1:
                contatore_modifiche_test = contatore_modifiche_test + (int(file_mod[0]) + int(file_mod[1]))
                flag_mrr = 1
                # cont_test_classes_modified = cont_test_classes_modified + 1
                try:
                    test_classes_modified.index(file_mod[2])
                    # print("Indice presente")
                    counter_tcm[test_classes_modified.index(file_mod[2])] = counter_tcm[test_classes_modified.index(
                        file_mod[2])] + 1
                except:
                    # print("Aggiunta alla lista")
                    test_classes_modified.append(file_mod[2])
                    counter_tcm.append(1)
            elif file_mod[2].find("Test") != -1:
                contatore_modifiche_test = contatore_modifiche_test + (int(file_mod[0]) + int(file_mod[1]))
                flag_mrr = 1
                # cont_test_classes_modified = cont_test_classes_modified + 1
                try:
                    test_classes_modified.index(file_mod[2])
                    # print("Indice presente")
                    counter_tcm[test_classes_modified.index(file_mod[2])] = counter_tcm[test_classes_modified.index(
                        file_mod[2])] + 1
                except:
                    # print("Aggiunta alla lista")
                    test_classes_modified.append(file_mod[2])
                    counter_tcm.append(1)
            contatore_modifiche = contatore_modifiche + (int(file_mod[0]) + int(file_mod[1]))
            # print(contatore_modifiche)
            # print(contatore_modifiche_test)
            # CHANGE metric
            try:
                change.index(file_mod[2])
                # print("Indice presente")
                counter_change[change.index(file_mod[2])] = counter_change[change.index(file_mod[2])] + (
                        int(file_mod[0]) + int(file_mod[1]))
            except:
                # print("Aggiunta alla lista")
                change.append(file_mod[2])
                counter_change.append((int(file_mod[0]) + int(file_mod[1])))
        cont_file = cont_file + 1
    if flag_mrr == 1:
        cont_taggedreleases_modified = cont_taggedreleases_modified + 1

        # TTL (vtag1)
        # TTL (Total Test LOCs):
        # provacheckout = r.git.checkout(vtag1)
        # Tdiffi(vtag2)
    # print(risultati_cloc.stdout)
    # print(output_cloc_loc)
    contout = 0
    plocsi = 0
    cloc = 0


    # print(diff)
    # print(diff_filtrata)
    # print('Modifiche totali (tra %s e %s): %d' % (vtag1, vtag2, contatore_modifiche))
    # print('Modifiche ai test (tra %s e %s): %d' % (vtag1, vtag2, contatore_modifiche_test))
    # diff_filtr2 = diff_filtrata.split("\ ")
    # print(diff_filtr2)


    # MTLR (Modified Test LOCs Ratio):
    # MTLR(i) = Tdiffi(i)/TTL(i-1)
    # MTLR(vtag2) = Tdiffi(vtag2)/TTL(vtag1)
    if ttl == 0:
        mtlr = 0
    else:
        mtlr = contatore_modifiche_test / ttl
    print('MTLR (Modified Test LOCs Ratio) %s: %.2f' % (vtag2, mtlr), flush=True)

    # MRTL (Modified Relative Test LOCs):
    # MRTL(i) = Tdiffi(i)/Pdiffi(i)
    # MRTL(i) = Tdiffi(vtag2)/Pdiffi(vtag2)
    if contatore_modifiche == 0:
        mrtl = 0
    else:
        mrtl = contatore_modifiche_test / contatore_modifiche
    print('MRTL (Modified Relative Test LOCs) %s: %.2f' % (vtag2, mrtl), flush=True)

    # MCR (Modified Test Classes Ratio):
    # MCR(i) = MC(i)/NTC(i-1)
    if len(ntc_iminusone) == 0:
        mcr = 0
    else:
        mcr = cont_test_classes_modified / len(ntc_iminusone)
    print("MCR (Modified Test Classes Ratio) %s: %.2f" % (vtag2, mcr), flush=True)
    metodi_rimossi = 0
    metodi_aggiunti = 0
    metodi_modificati = 0
    tm_i = 0
    tm_imenouno = 0
    mmr = 0
    mcmm = 0
    for i in ntc:
        if os.path.exists(local_rep_iminusone + i) and os.path.exists(local_rep_i + i):
            if i.find(".kt") == -1:
                # print(i)
                diff_file = ".\\input\\diff.txt"

                j = i.replace("/", "\\")

                output_diff_file = r.git.diff(vtag1, vtag2, j)
                # differences = output_diff.split('\n')
                # print(differences)
                # file_differences = open("diff.txt", "w", encoding="utf8")
                # file_differences.write(output_diff_file)
                # file_differences.close()
                with open(".\\input\\diff.txt", "w", encoding="utf-8", errors="surrogateescape") as f:
                    f.write(output_diff_file)

                methods_analysis = subprocess.run(['java', '-jar', '.\input\parser_1401.jar', local_rep_iminusone + j,
                                                   local_rep_i + j,
                                                   diff_file], stdout=subprocess.PIPE)
                risultato_metodi = methods_analysis.stdout.decode('utf8').split('\n')
                if risultato_metodi[0] != '':
                    metodi_rimossi = metodi_rimossi + int(risultato_metodi[0].split(' ')[-1])
                    metodi_aggiunti = metodi_aggiunti + int(risultato_metodi[1].split(' ')[-1])
                    if int(risultato_metodi[2].split(' ')[-1]) != 0:
                        mcmm = mcmm + 1
                    metodi_modificati = metodi_modificati + int(risultato_metodi[2].split(' ')[-1])
                    tm_i = tm_i + int(risultato_metodi[3].split(' ')[-1])
                    tm_imenouno = tm_imenouno + int(risultato_metodi[4].split(' ')[-1])
                # print(risultato_metodi)
            else:
                # PARTE ANALISI KOTLIN METODI
                # print(i)
                diff_file = ".\\input\\diff.txt"
                cont_metodi_kt_added = 0
                cont_metodi_kt_removed = 0
                cont_metodi_kt_modified = 0
                cont_metodi_kt_iminusone = 0
                cont_metodi_kt_i = 0
                flag_method_add = 0
                flag_method_rem = 0
                j = i.replace("/", "\\")
                output_diff_file_kt = r.git.diff(vtag1, vtag2, j)
                # differences = output_diff.split('\n')
                # print(differences)
                file_differences_kt = open(".\\input\\diff.txt", "w", encoding="utf8", errors="surrogateescape")
                file_differences_kt.write(output_diff_file_kt)
                file_differences_kt.close()
                file_kt_iminusone = open(local_rep_iminusone + j, "r", encoding="utf8", errors="surrogateescape")
                content_file_kt_iminusone = file_kt_iminusone.readlines()
                file_kt_i = open(local_rep_i + j, "r", encoding="utf8", errors="surrogateescape")
                content_file_kt_i = file_kt_i.readlines()
                for riga_file_iminusone in content_file_kt_iminusone:
                    if riga_file_iminusone.find("fun") != -1:
                        flag_method_rem = 0
                        cont_metodi_kt_iminusone = cont_metodi_kt_iminusone + 1
                        for riga_file_i in content_file_kt_i:
                            if riga_file_iminusone == riga_file_i:
                                flag_method_rem = 1
                        if flag_method_rem == 0:
                            cont_metodi_kt_removed = cont_metodi_kt_removed + 1
                for riga_file_i in content_file_kt_i:
                    if riga_file_i.find("fun") != -1:
                        flag_method_add = 0
                        cont_metodi_kt_i = cont_metodi_kt_i + 1
                        for riga_file_iminusone in content_file_kt_iminusone:
                            if riga_file_i == riga_file_iminusone:
                                flag_method_add = 1
                        if flag_method_add == 0:
                            cont_metodi_kt_added = cont_metodi_kt_added + 1

                file_txt = open(diff_file, "r", encoding="utf8", errors="surrogateescape")
                content_file_txt = file_txt.readlines()
                for riga_txt in content_file_txt:
                    if riga_txt[0] == "@" and riga_txt[1] == "@":
                        split_one = riga_txt.split(" ")
                        split_two = split_one[2].split(",")
                        riga_aggiunta = int(split_two[0])
                        # content_file_kt_i[riga_aggiunta]
                        index = 0
                        cont_fun1 = 0
                        cont_fun2 = 0
                        flag_fun_primavolta = 0
                        for rigafilemod in content_file_kt_i:
                            if rigafilemod.find("fun") != -1:
                                if flag_fun_primavolta == 0:
                                    cont_fun1 = index
                                    flag_fun_primavolta = 1
                                else:
                                    cont_fun2 = index
                                if cont_fun2 != 0:
                                    if cont_fun1 <= riga_aggiunta < cont_fun2:
                                        cont_metodi_kt_modified = cont_metodi_kt_modified + 1
                                        break
                            index = index + 1
                # print("Added methods kt: %d" % cont_metodi_kt_added)
                # print("Removed methods kt: %d" % cont_metodi_kt_removed)
                # print("Modified methods kt: %d" % cont_metodi_kt_modified)
                # print("I methods kt: %d" % cont_metodi_kt_i)
                # print("I minus one methods kt: %d" % cont_metodi_kt_iminusone)
                metodi_rimossi = metodi_rimossi + cont_metodi_kt_removed
                metodi_aggiunti = metodi_aggiunti + cont_metodi_kt_added
                if cont_metodi_kt_modified != 0:
                    mcmm = mcmm + 1
                metodi_modificati = metodi_modificati + cont_metodi_kt_modified
                tm_i = tm_i + cont_metodi_kt_i
                tm_imenouno = tm_imenouno + cont_metodi_kt_iminusone
                file_kt_iminusone.close()
                file_kt_i.close()
                file_txt.close()
        else:
            if i.find(".kt") == -1:
                # print(i)
                j = i.replace("/", "\\")
                methods_analysis_only_add = subprocess.run(['java', '-jar', '.\input\parser_1401.jar',
                                                            local_rep_i + j], stdout=subprocess.PIPE)
                risultato_metodi_only_add = methods_analysis_only_add.stdout.decode('utf8').split('\n')
                if risultato_metodi_only_add[0].split(' ')[-1] != '':
                    tm_i = tm_i + int(risultato_metodi_only_add[0].split(' ')[-1])
                # print(risultato_metodi_only_add)
            else:
                # PARTE ANALISI KOTLIN METODI SOLO ADD
                # print(i)
                cont_metodi_kt_i = 0
                j = i.replace("/", "\\")
                file_kt_i = open(local_rep_i + j, "r", encoding="utf8", errors="surrogateescape")
                content_file_kt_i = file_kt_i.readlines()
                for riga_file_i in content_file_kt_i:
                    if riga_file_i.find("fun") != -1:
                        cont_metodi_kt_i = cont_metodi_kt_i + 1
                tm_i = tm_i + cont_metodi_kt_i
                # print("New methods kt: %d" % cont_metodi_kt_i)

    # MMR (Modified Test Methods Ratio):
    # MMR(i) = MM(i)/TM(i-1)
    # MMR(i) = Number of test methods modified between i-1 and i / Number of test methods (i-1)
    if tm_iminusone == 0:
        mmr = 0
    else:
        mmr = metodi_modificati / tm_iminusone
    print("MMR (Modified Test Methods Ratio) %s: %.2f" % (vtag2, mmr), flush=True)

    # MCMMR (Modified Classes with Modified Methods Ratio):
    # MCMMR(i) = MCMM(i)/NTC(i-1)
    # MCMMR(i) = Classed modified with at least one method modified between i-1 and i / Number of test classes (i-1)
    if len(ntc_iminusone) == 0:
        mcmmr = 0
    else:
        mcmmr = mcmm / len(ntc_iminusone)
    print("MCMMR (Modified Classes with Modified Methods Ratio) %s: %.2f" % (vtag2, mcmmr), flush=True)

    # print(ntct)
    # print(test_classes_modified)
    # print(counter_tcm)
    ntc_iminusone = ntc
    tm_iminusone = tm_i
    # print(tm_iminusone)

    # PARTE 3: CALCOLO METRICHE QUALITA' CODICE (N.B. SonarQube deve essere avviato prima)
    # print('Calcolo metriche di qualità del codice:')
    cc = 0
    td = 0
    nom = 0
    stat = 0
    code_smells = 0
    noc = 0
    nof = 0
    debt_ratio = 0
    changecsv = ''
    # 1. Copiare file .properties dentro cartella progetto vtag2
    file_prop = ".\\input\\sonar-project.properties"
    dove_cop = ".\\input\\ProgettoDaAnalizzareVtag2"
    copy_properties = subprocess.call(['copy', '.\input\sonar-project.properties', dove_cop], shell=True)
    # 2. Far partire scansione sonar-scanner.bat (con cambio di directory)
    start_scanner = subprocess.run(["cd", ".\input\ProgettoDaAnalizzareVtag2", "&&", "..\\sonar-scanner\\bin\\sonar-scanner.bat"],
                                   stdout=subprocess.PIPE, shell=True)
    # 3. analizzare risultati json
    sleep(10)
    url = "http://localhost:9000/api/measures/component?metricKeys=complexity,sqale_index,functions,statements,code_smells,classes,files,sqale_debt_ratio,ncloc,comment_lines&component=" + nome_progetto_sonarqube
    resp = requests.get(url, auth=HTTPBasicAuth('admin', 'admin1'))
    sonar_resp_json = resp.json()
    # print(sonar_resp_json)
    metriche = sonar_resp_json["component"]["measures"]
    # print(metriche)

    for i in metriche:
        if i['metric'] == 'complexity':
            # print(i['value'])
            cc = int(i['value'])
        if i['metric'] == 'sqale_index':
            # print(i['value'])
            td = int(i['value'])
        if i['metric'] == 'functions':
            # print(i['value'])
            nom = int(i['value'])
        if i['metric'] == 'statements':
            # print(i['value'])
            stat = int(i['value'])
        if i['metric'] == 'code_smells':
            # print(i['value'])
            code_smells = int(i['value'])
        if i['metric'] == 'classes':
            # print(i['value'])
            noc = int(i['value'])
        if i['metric'] == 'files':
            # print(i['value'])
            nof = int(i['value'])
        if i['metric'] == 'sqale_debt_ratio':
            # print(i['value'])
            debt_ratio = float(i['value'])
        if i['metric'] == 'ncloc':
            # print(i['value'])
            plocsi = int(i['value'])
        if i['metric'] == 'comment_lines':
            # print(i['value'])
            cloc = int(i['value'])

    # TLR (Test LOCs Ratio):
    if plocsi == 0:
        tlr = 0
    else:
        tlr = ttl / plocsi

    # print("PLOCS: ", plocsi)
    # print("vecchio PLOCS: ", plocs)
    print('TLR (Total LOCs Ratio) %s: %.2f' % (vtag2, tlr), flush=True)
    # 4. stampare metriche
    print("----------------------------------------------------------------------", flush=True)
    print("Metriche qualità codice (versioni %s - %s): " % (vtag1, vtag2), flush=True)
    print("----------------------------------------------------------------------", flush=True)
    print("CHANGE (Number of Lines Changed in the Class) %s: " % vtag2, flush=True)
    for i in range(len(change)):
        print(change[i], counter_change[i], flush=True)
        changecsv = str(changecsv) + str(str(change[i]) + "(" + str(counter_change[i]) + ") ")

    print("LOC (Lines of Code) %s: %d" % (vtag2, plocsi), flush=True)
    print("CLOC (Comment Lines of Code) %s: %d" % (vtag2, cloc), flush=True)
    print("CC (McCabe’s Cyclomatic Complexity) %s: %d" % (vtag2, cc), flush=True)
    print("TD (Technical Debt) %s: %d" % (vtag2, td), flush=True)
    print("NOM (Number of Methods) %s: %d" % (vtag2, nom), flush=True)
    print("STAT (Number of Statements) %s: %d" % (vtag2, stat), flush=True)
    print("CODE SMELLS %s: %d" % (vtag2, code_smells), flush=True)
    print("NOC (Number of Classes) %s: %d" % (vtag2, noc), flush=True)
    print("NOF (Number of Files) %s: %d" % (vtag2, nof), flush=True)
    print("TECHNICAL DEBT RATIO %s: %.2f" % (vtag2, debt_ratio), flush=True)

    del_sonar_properties = subprocess.run(["cd", ".\\input\\ProgettoDaAnalizzareVtag2", "&&", "del", ".\\sonar-project.properties"],
                                          stdout=subprocess.PIPE, shell=True)

    with open('.\\input\\' + rep_git_csv + '.csv', 'a') as csvfile:
        writer = csv.writer(csvfile, delimiter=',')
        writer.writerow((vtag2, len(ntc), ttl, tlr, mtlr, mrtl, mcr, mmr, mcmmr, cc, changecsv, code_smells, td,
                         debt_ratio, cloc, plocsi, noc, nof, nom, stat))

    # AGGIORNAMENTO VTAG1 E VTAG2
    vtag1 = tags[cont_tags]
    cont_tags = cont_tags + 1
    if cont_tags <= len(tags) - 1:
        vtag2 = tags[cont_tags]

# MRR (Modified Releases Ratio):
# MRR = Number of tagged releases in which at least one test class is modified / NTR
# MRR = cont_taggedreleases_modified / NTR
mrr = cont_taggedreleases_modified / len(tags)
print("----------------------------------------------------------------------", flush=True)
print("MRR (Modified Releases Ratio):  %.2f" % mrr, flush=True)

# TSV (Test Suite Volatility):
# TSV = Number of test classes modified / Number of Test Classes in the History
newNumberofTestClassesModified = 0
for i in counter_tcm:
    # if counter_tcm[i] > 1:
    if i > 1:
        newNumberofTestClassesModified = newNumberofTestClassesModified + 1
if len(ntct) == 0:
    tsv = 0
else:
    tsv = newNumberofTestClassesModified / len(ntct)
print("TSV (Test Suite Volatility): %.2f (num = %d, den = %d)" % (tsv, newNumberofTestClassesModified, len(ntct)), flush=True)

with open('.\\input\\' + rep_git_csv + '_2.csv', 'a') as csvfile:
    writer = csv.writer(csvfile, delimiter=',')
    writer.writerow((len(tags), mrr, tsv))

# PULIZIA
pulizia(local_rep_iminusone)
pulizia(local_rep_i)
pulizia(local_rep)
