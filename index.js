const vm = require('vm');

class Thread {
	constructor(func) {
		this.func = func;
		this.args;
		this.loopInt = 0;
	}

	params(...args) {
		this.args = args;
	}

	loop(i) {
		this.loopInt = i;
	}

	async start()  {
		if(this.loopInt == 0) {
			this.func(...this.args);
		} else {
			setInterval(() => {
				this.func(...this.args);
			}, this.loopInt);
		}
	}
}

let sandbox = {
  require,
  console
};

let t = new Thread(async (v, sand) => {
	console.log("fgargah");
});

t.loop(1000);
t.params(vm, sandbox);
t.start();

module.exports.thread = Thread;
