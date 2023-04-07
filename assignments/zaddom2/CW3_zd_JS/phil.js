// Teoria Współbieżnośi, implementacja problemu 5 filozofów w node.js
// Opis problemu: http://en.wikipedia.org/wiki/Dining_philosophers_problem
//   https://pl.wikipedia.org/wiki/Problem_ucztuj%C4%85cych_filozof%C3%B3w
// 1. Dokończ implementację funkcji podnoszenia widelca (Fork.acquire).
// 2. Zaimplementuj "naiwny" algorytm (każdy filozof podnosi najpierw lewy, potem
//    prawy widelec, itd.).
// 3. Zaimplementuj rozwiązanie asymetryczne: filozofowie z nieparzystym numerem
//    najpierw podnoszą widelec lewy, z parzystym -- prawy. 
// 4. Zaimplementuj rozwiązanie z kelnerem (według polskiej wersji strony)
// 5. Zaimplementuj rozwiążanie z jednoczesnym podnoszeniem widelców:
//    filozof albo podnosi jednocześnie oba widelce, albo żadnego.
// 6. Uruchom eksperymenty dla różnej liczby filozofów i dla każdego wariantu
//    implementacji zmierz średni czas oczekiwania każdego filozofa na dostęp 
//    do widelców. Wyniki przedstaw na wykresach.

var Fork = function() {
    this.state = 0;
    return this;
}

Fork.prototype.acquire = function(cb) { 
    // zaimplementuj funkcję acquire, tak by korzystala z algorytmu BEB
    // (http://pl.wikipedia.org/wiki/Binary_Exponential_Backoff), tzn:
    // 1. przed pierwszą próbą podniesienia widelca Filozof odczekuje 1ms
    // 2. gdy próba jest nieudana, zwiększa czas oczekiwania dwukrotnie
    //    i ponawia próbę, itd.

    var fork = this;
    var getBEB = (time) => {
        setTimeout( () => {
            if(fork.state == 0) {
                fork.state = 1;
                cb();
            }
            else {
                getBEB( time*2);
            }
        }, time);
    };

    getBEB(1);

}

Fork.prototype.release = function() { 
    this.state = 0; 
}

var Philosopher = function(id, forks) {
    this.id = id;
    this.forks = forks;
    this.f1 = id % forks.length;
    this.f2 = (id+1) % forks.length;
    this.fullTime = 0;
    return this;
}

Philosopher.prototype.startNaive = function(count) {
    var forks = this.forks,
        f1 = this.f1,
        f2 = this.f2,
        id = this.id,
    
    // zaimplementuj rozwiązanie naiwne
    // każdy filozof powinien 'count' razy wykonywać cykl
    // podnoszenia widelców -- jedzenia -- zwalniania widelców

    naiveSolution = (count) => {
        var tmpTime = new Date().getTime();
        if(count > 0){
            forks[f1].acquire(() => {
                forks[f2].acquire( () => {
                    this.fullTime += new Date().getTime - tmpTime;
                    forks[f1].release();
                    forks[f2].release();
                    setTimeout( () => {
                        naiveSolution(count-1)
                    }, 1)
                })
            })
        }
    }

    naiveSolution(count);

    // solution with deadlocks
}

Philosopher.prototype.startAsym = function(count) {
    var forks = this.forks,
        f1 = this.f1,
        f2 = this.f2,
        id = this.id;

    if(id % 2 != 0){
        f1 = this.f2;
        f2 = this.f1;
    }
    
    // zaimplementuj rozwiązanie asymetryczne
    // każdy filozof powinien 'count' razy wykonywać cykl
    // podnoszenia widelców -- jedzenia -- zwalniania widelców

    asymSolution = (count) => {
        var tmpTime = new Date().getTime();
        if(count > 0){
            forks[f1].acquire(() => {
                forks[f2].acquire( () => {
                    this.fullTime += (new Date().getTime() - tmpTime);
                    setTimeout( () => {
                        forks[f1].release();
                        forks[f2].release();
                        asymSolution(count-1);
                    }, 1)
                })
            })
        }
        else {
            --notReady;
            if(notReady == 0){
                afterr();
            }
        }
        
    }

    asymSolution(count);

}

Philosopher.prototype.startConductor = function(count, conductor) {
    this.fullTime = 0;
    var forks = this.forks,
        f1 = this.f1,
        f2 = this.f2,
        id = this.id,
    
    // zaimplementuj rozwiązanie z kelnerem
    // każdy filozof powinien 'count' razy wykonywać cykl
    // podnoszenia widelców -- jedzenia -- zwalniania widelców

    conductorSolution = (count) => {
        var tmpTime = new Date().getTime();
        if (count > 0) {
            conductor.takePlace(id, () => {
                forks[f1].acquire(() => {
                    forks[f2].acquire( () => {
                        this.fullTime += new Date().getTime - tmpTime;
                        forks[f1].release();
                        forks[f2].release();
                        conductor.emptyPlace
                        setTimeout( () => {
                            conductor.emptyPlace(id, () => {
                                conductorSolution(count-1);
                            })
                            
                        }, 1)
                    })
                })
            })
        }
    };
   
    conductorSolution(count);
  
}

Conductor = function(n){
    this.philosophersTable = [];
    this.waitingCallbacks = [];
    this.waitingPhilosophers = [];

    for(let i = 0; i < n; i++){
        this.philosophersTable.push(0);
    }
    return this;
}

Conductor.prototype.availablePlace = () => {
    var n = this.philosophersTable.length,
    amount = 0;

    for(let i=0; i < n; i++){
        if(this.philosophers[i]== 1){
            amount++;
        }
    }

    return amount <= 2;
}

Conductor.prototype.takePlace = (philId, cb) => {
    if(this.availablePlace){
        this.philosophersTable[philId] = 1;
        setTimeout(cb);
    }
    else {
        this.waitingCallbacks.unshift(cb);
        this.waitingPhilosophers.unshift(cb);
    }
}

Conductor.prototype.emptyPlace = (philId, cb) => {
    this.philosophersTable[philId] = 0;

    while(this.waitingCallbacks.length > 0 && this.canAccessNow()) {
        var prev = this.waitingPhilosophers.pop(),
        prevCb = this.waitingCallbacks.pop();
    
    this.philosophersTable[prev] = 1;
    setTimeout(prevCb);
    }
    setTimeout(cb);
}


// // TODO: wersja z jednoczesnym podnoszeniem widelców
// // Algorytm BEB powinien obejmować podnoszenie obu widelców, 
// // a nie każdego z osobna

acquire2 = (fork1, fork2, cb) => {
    var rek = (time) => {
        setTimeout( () => {
            if(fork1.state == 0 && fork2.state == 0) {
                fork1.state = 1;
                fork2.state = 1;
                cb();
            }
            else {
                rek(time*2);
            }
        }, time);
    };

    rek(1);
}

Philosopher.prototype.startBoth = function(count) {
    this.fullTime = 0;
    var forks = this.forks,
        f1 = this.f1,
        f2 = this.f2,
        id = this.id,

    bothSolutioin = (count) => {
        var tmpTime = new Date().getTime();
        if(count > 0)
        acquire2(forks[f1], forks[f2], () => {
            this.fullTime += new Date().getTime() - tmpTime;
            forks[f1].release();
            forks[f2].release();
            setTimeout(() => {
                bothSolutioin(count - 1);
            }, 1)
        })
        else {
            --notReady;
            if(notReady == 0){
                afterr();
            }
        }
    }

    bothSolutioin(count);
}

var N = 9;
var forks = [];
var meals = 20;
var philosophers = []
var notReady;
var afterr;
for (var i = 0; i < N; i++) {
    forks.push(new Fork());
}

for (var i = 0; i < N; i++) {
    philosophers.push(new Philosopher(i, forks));
}

var conductor = new Conductor(N);

for (var i = 0; i < N; i++) {
    philosophers[i].startAsym(10);
}

notReady = N;
afterr = function (){
    console.log("asym: ");

    for (var i = 0; i < N; i++) {
        console.log(philosophers[i].fullTime/meals);
    }

    for (var i = 0; i < N; i++) {
        philosophers[i].startBoth(10);
    }
    notReady = N;
    afterr = function(){
        console.log("both: ");

    for (var i = 0; i < N; i++) {
        console.log(philosophers[i].fullTime/meals);
    }
    }
}

