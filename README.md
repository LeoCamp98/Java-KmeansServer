Lato server del progetto Kmeans. L'attività di clustering si articola in diversi passaggi:

  1. inizializzazione di x segmenti (insiemi vuoti)
  2. inizializzazione dei centroidi: si scelgono x transazioni in maniera casuale e le si inseriscono nei segmenti, in maniera tale da avere un centroide per segmento
  3. assegnazione di ciascuna transazione al suo cluster: l'appartenenza di una transazione ad un cluster dipende dalla distanza della transazione dal centroide del cluser (viene scelto il cluster che minimizza tale distanza)
  4. ricalcolo dei centroidi dei cluster
  5. se ci sono transazioni che hanno cambiato cluster di appartenenza ripeto dal passo 3
