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

    var fork = this,
	loop = function (waitTime) {
	    setTimeout(function () {
		if (fork.state == 0) {
		    fork.state = 1;
		    cb();
		}
		else
		    loop (waitTime * 2);
 	    }, waitTime);
	};

    loop(1); // jednomilisekundowy timeout na początek
}

Fork.prototype.release = function() { 
    this.state = 0; 
}

var Philosopher = function(id, forks) {
    this.id = id;
    this.forks = forks;
    this.f1 = id % forks.length;
    this.f2 = (id+1) % forks.length;
    this.totalWaitTime = 0;
    this.currentWaitStartTime = -1;
    
    return this;
}

Philosopher.prototype.startNaive = function(count) {
    this.totalWaitTime = 0;
    
    var forks = this.forks,
        f1 = this.f1,
        f2 = this.f2,
        id = this.id,
	myself = this,
    
	// zaimplementuj rozwiązanie naiwne
	// każdy filozof powinien 'count' razy wykonywać cykl
	// podnoszenia widelców -- jedzenia -- zwalniania widelców

	loopNaive = function (count) {
	    myself.currentWaitStartTime = new Date().getTime();
	    if (count > 0)
		forks[f1].acquire(function () {
		    console.log("filozof " + id + " wziął lewy widelec");
		    forks[f2].acquire(function () {
			myself.totalWaitTime += new Date().getTime() - myself.currentWaitStartTime;
			console.log("filozof " + id + " wziął prawy widelec i zaczyna jeść");
			setTimeout(function () {
			    forks[f1].release();
			    forks[f2].release();
			    console.log("filozof " + id + " kończy jeść");
			    // zakładamy, że myślenie jest reprezentowane przez pierwszy,
			    // jednomilisekundowy timeout w fork.acquire()
			    loopNaive(count - 1);
			}, 1) // zakładamy, że jedzenie trwa 1 milisekundę
		    })})
	};

    // Startuję ich w losowych momentach, dzięki czemu nie zakleszczą
    // się od razu, tylko po kilku jedzeniach i dostaniemy ciekawszy output :)
    setTimeout(function () {loopNaive(count)}, Math.floor(Math.random() * 10));
}

Philosopher.prototype.startAsym = function(count) {
    this.totalWaitTime = 0;

    var forks = this.forks,
        f1 = this.id % 2 == 0 ? this.f1 : this.f2,
        f2 = this.id % 2 == 0 ? this.f2 : this.f1,
        id = this.id,
	myself = this,
	
	// zaimplementuj rozwiązanie asymetryczne
	// każdy filozof powinien 'count' razy wykonywać cykl
	// podnoszenia widelców -- jedzenia -- zwalniania widelców

	loopAsym = function (count) {
	    myself.currentWaitStartTime = new Date().getTime();
	    if (count > 0)
		forks[f1].acquire(function () {
		    console.log("filozof " + id + " wziął lewy widelec");
		    forks[f2].acquire(function () {
			myself.totalWaitTime += new Date().getTime() - myself.currentWaitStartTime;
			console.log("filozof " + id + " wziął prawy widelec i zaczyna jeść");
			setTimeout(function () {
			    forks[f1].release();
			    forks[f2].release();
			    console.log("filozof " + id + " kończy jeść");
			    // zakładamy, że myślenie jest reprezentowane przez pierwszy,
			    // jednomilisekundowy timeout w fork.acquire()
			    loopAsym(count - 1);
			}, 1) // zakładamy, że jedzenie trwa 1 milisekundę
		    })})
	    else {
		--philosophersNotYetFinished
		if (philosophersNotYetFinished == 0)
		    cbOnEnd();
	    }
	};

    setTimeout(function () {loopAsym(count)}, 1);
}

Philosopher.prototype.startKelner = function(count, kernel) {
    this.totalWaitTime = 0;

    var forks = this.forks,
        f1 = this.f1,
        f2 = this.f2,
        id = this.id,
	myself = this,
    
	// zaimplementuj rozwiązanie z kelnerem
	// każdy filozof powinien 'count' razy wykonywać cykl
	// podnoszenia widelców -- jedzenia -- zwalniania widelców

	loopKelner = function (count) {
	    myself.currentWaitStartTime = new Date().getTime();
	    if (count > 0)
		kernel.accessTable(id, function () {
		    console.log("filozof " + id + " podchodzi do stołu");
		    forks[f1].acquire(function () {
			console.log("filozof " + id + " wziął lewy widelec");
			forks[f2].acquire(function () {
			    myself.totalWaitTime += new Date().getTime() - myself.currentWaitStartTime;
			    console.log("filozof " + id + " wziął prawy widelec i zaczyna jeść");
			    setTimeout(function () {
				forks[f1].release();
				forks[f2].release();
				console.log("filozof " + id + " kończy jeść i odchodzi od stołu");
				kernel.leaveTable(id, function () {
				    loopKelner(count - 1);
				});
			    }, 1) // zakładamy, że jedzenie trwa 1 milisekundę
			})})})
	    else {
		--philosophersNotYetFinished
		if (philosophersNotYetFinished == 0)
		    cbOnEnd();
	    }
	};

    setTimeout(function () {loopKelner(count)}, 1);
}


// TODO: wersja z jednoczesnym podnoszeniem widelców
// Algorytm BEB powinien obejmować podnoszenie obu widelców, 
// a nie każdego z osobna

function acquire2(fork1, fork2, cb) {
    var loop = function (waitTime) {
	setTimeout(function () {
	    if (fork1.state == 0 && fork2.state == 0) {
		fork1.state = fork2.state = 1;
		cb();
	    }
	    else
		loop (waitTime * 2);
	}, waitTime);
    };

    loop(1); // jednomilisekundowy timeout na początek
}

Philosopher.prototype.startSimult = function(count) {
    this.totalWaitTime = 0;
    
    var forks = this.forks,
        f1 = this.f1,
        f2 = this.f2,
        id = this.id,
	myself = this,
    
	// zaimplementuj rozwiązanie naiwne
	// każdy filozof powinien 'count' razy wykonywać cykl
	// podnoszenia widelców -- jedzenia -- zwalniania widelców

	loopSimult = function (count) {
	    myself.currentWaitStartTime = new Date().getTime();
	    if (count > 0)
		acquire2(forks[f1], forks[f2], function () {
		    myself.totalWaitTime += new Date().getTime() - myself.currentWaitStartTime;
		    console.log("filozof " + id + " wziął widelce i zaczyna jeść");
		    setTimeout(function () {
			forks[f1].release();
			forks[f2].release();
			console.log("filozof " + id + " kończy jeść");
			// zakładamy, że myślenie jest reprezentowane przez pierwszy,
			// jednomilisekundowy timeout w acquire2()
			loopSimult(count - 1);
		    }, 1) // zakładamy, że jedzenie trwa 1 milisekundę
		})
	    else {
		--philosophersNotYetFinished
		if (philosophersNotYetFinished == 0)
		    cbOnEnd();
	    }
	};
    
    setTimeout(function () {loopSimult(count)}, 1);
}

Kelner = function (N) {
    this.philosophersAtTable = [];
    this.waitingCallbacks = [];
    this.waitingPhilosophers = [];

    for (var i = 0; i < N; i++) {
	this.philosophersAtTable.push(0);
    }
    
    return this;
}

Kelner.prototype.canAccessNow = function() {
    var neighboursAtTable = false,
	howManyAtTable = 0,

	N = this.philosophersAtTable.length,
	previousPresent = this.philosophersAtTable[N - 1] == 1;

    for (var i = 0; i < N; i++) {
	if (this.philosophersAtTable[i] == 1) {
	    if (previousPresent)
		neighboursAtTable = true;
	    previousPresent = true;
	    howManyAtTable++;
	}
	else
	    previousPresent = false;
    }
    
    return howManyAtTable < 2 || (howManyAtTable == 2 && neighboursAtTable);
}

Kelner.prototype.accessTable = function(philosopherId, cb) {
    if (this.canAccessNow()) {
	this.philosophersAtTable[philosopherId] = 1;
	setTimeout(cb);
    }
    else {
	var pushBack = function(arr, item) {
	    arr.push(item);
	    for (var i = arr.length - 1; i > 0; i--) {
		arr[i] = arr[i - 1];
	    }
	    arr[0] = item;
	};

	pushBack(this.waitingCallbacks, cb);
	pushBack(this.waitingPhilosophers, philosopherId);
    }
}

Kelner.prototype.leaveTable = function(philosopherId, cb) {
    this.philosophersAtTable[philosopherId] = 0;

    while (this.waitingCallbacks.length > 0 && this.canAccessNow()) {
	var philosopherIdFromList = this.waitingPhilosophers.pop(),
	    cbFromList = this.waitingCallbacks.pop();
	
	this.philosophersAtTable[philosopherIdFromList] = 1;
	setTimeout(cbFromList);
    }

    setTimeout(cb);
}

var N;
var meals;

var forks = [];
var philosophers = []

var philosophersNotYetFinished;
var cbOnEnd;

const rl = require('readline').createInterface({
  input: process.stdin,
  output: process.stdout
});

rl.question('Podaj liczbę filozofów: ', (NPhilosophers) => {
    N = parseInt(NPhilosophers);
    rl.question('Podaj, ile razy każdy będzie jadł: ', (NMeals) => {
	meals = parseInt(NMeals);
	rl.close();

	for (var i = 0; i < N; i++) {
	    forks.push(new Fork());
	}

	for (var i = 0; i < N; i++) {
	    philosophers.push(new Philosopher(i, forks));
	}

	var kernel = new Kelner(philosophers);

	function printAvgTimes() {
	    for (var i = 0; i < N; i++) {
		console.log(i + " czekał średnio "
			    + philosophers[i].totalWaitTime / meals
			    + "ms");
	    }
	    console.log("");
	}

	for (var i = 0; i < N; i++) {
	    philosophers[i].startAsym(meals);
	}

	philosophersNotYetFinished = N;
	cbOnEnd = function () {
	    console.log("wyniki Asym:");
	    printAvgTimes();
	    
	    for (var i = 0; i < N; i++) {
		philosophers[i].startSimult(meals);
	    }
	    
	    philosophersNotYetFinished = N;
	    cbOnEnd = function () {
		console.log("\nwyniki Simult:");
		printAvgTimes();
		
		for (var i = 0; i < N; i++) {
		    philosophers[i].startKelner(meals, kernel);
		}
		
		philosophersNotYetFinished = N;
		cbOnEnd = function () {
		    console.log("\nwyniki Kelner:");
		    printAvgTimes();
		}
	    }
	}	
    });
});

/** I have to admit, that I don't recall having ever written more *
  * a spaghetti of code than this one...                          **/
