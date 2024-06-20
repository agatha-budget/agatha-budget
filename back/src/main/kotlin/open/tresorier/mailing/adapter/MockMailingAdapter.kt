package open.tresorier.mailing.adapter

import open.tresorier.mailing.IMailingPort

import open.tresorier.model.Person

class MockMailingAdapter() : IMailingPort {
    
    override fun add(person: Person) {
        System.out.println("Let's pretend we did it");
	}
}