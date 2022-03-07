import csv
import os
import shutil
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
        print("Error cleaning repository: %s : %s" % (repository, e.strerror))
        sleep(10)
        try:
            shutil.rmtree(repository, onerror=readonly_handler)
        except OSError as e:
            print("Error cleaning repository (2nd time): %s : %s" % (repository, e.strerror))


contatore_cartelle = 0
contatore_progetti = 0
with open('.\input\projects.csv', 'w') as csvfile:
    writer = csv.writer(csvfile, delimiter=',')
authors = os.listdir(".\\input\\only_test_classes")
while contatore_cartelle <= len(authors) - 1:
    # print(authors)
    author = authors[contatore_cartelle]
    if os.path.isdir(".\\input\\only_test_classes\\" + author):
        names = os.listdir(".\\input\\only_test_classes\\" + author)
        name = names[0]
        # print(name)
        project_repo = "git://github.com/" + author + "/" + name + ".git"
        local_rep = ".\\input\\"+name+"\\"
        try:
            Repo.clone_from(project_repo, local_rep)
            repository_git = git.Repo(local_rep)
            # if repository_git
            output_ntr = repository_git.git.tag(sort='creatordate')
            tags = output_ntr.split('\n')
            # print(tags)

            output_ntc = repository_git.git.grep("-l", ".\\input\\")
            file_totali_progetto = output_ntc.split('\n')
            ntc = []
            cont_file = 0
            while cont_file < len(file_totali_progetto):
                if file_totali_progetto[cont_file].find('.java') != -1:
                    if file_totali_progetto[cont_file].find('test') != -1:
                        ntc.append(file_totali_progetto[cont_file].split('\t')[-1])
                    elif file_totali_progetto[cont_file].find('Test') != -1:
                        ntc.append(file_totali_progetto[cont_file].split('\t')[-1])
                elif file_totali_progetto[cont_file].find('.kt') != -1:
                    if file_totali_progetto[cont_file].find('test') != -1:
                        ntc.append(file_totali_progetto[cont_file].split('\t')[-1])
                    elif file_totali_progetto[cont_file].find('Test') != -1:
                        ntc.append(file_totali_progetto[cont_file].split('\t')[-1])
                cont_file = cont_file + 1
            # print(ntc)
            ultimo_commit = repository_git.git.log('-1', format='%cs')
            # print(ultimo_commit)
            pulizia(local_rep)
            contatore_progetti = contatore_progetti + 1
            print("%d) Nome progetto: %s, Versioni totali: %d" % (contatore_progetti, project_repo, len(tags)))
            print("    Numero classi di test: %d, Data ultimo commit: %s" % (len(ntc), ultimo_commit))
            with open('.\input\projects.csv', 'a') as csvfile:
                writer = csv.writer(csvfile, delimiter=',')
                writer.writerow((project_repo, len(tags), len(ntc), ultimo_commit))
        except:
            print("Repository doesn't exist: %s" % project_repo)
    contatore_cartelle = contatore_cartelle + 1
print(contatore_progetti)
